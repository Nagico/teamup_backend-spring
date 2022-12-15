import json

import findspark
import numpy as np
from numpy.linalg import linalg
from pyspark.ml.recommendation import ALS
from pyspark.sql.functions import udf
from pyspark.sql.types import FloatType

findspark.init()

import pandas as pd
from pyspark.sql import SparkSession


class Spark:
    def __init__(self):
        self.sc = SparkSession.builder.appName("TeamUp").master("spark://server:17077").getOrCreate()

    def __del__(self):
        self.sc.stop()

    def train_als(self, rating_matrix):
        rating = []
        for i in range(len(rating_matrix)):
            for j in range(len(rating_matrix[i])):
                if rating_matrix[i][j] > 0:
                    rating.append([i, j, rating_matrix[i][j]])

        df = pd.DataFrame(rating, columns=["user", "item", "rating"])

        df = self.sc.createDataFrame(df, schema=["user", "item", "rating"])

        als = ALS(rank=10, maxIter=10, regParam=0.01, coldStartStrategy="nan")
        model = als.fit(df)
        return model

    @staticmethod
    @udf(returnType=FloatType())
    def cal_user_sim(v1, v2):
        grade = 1 / (abs(v2[0] - v1[0]) + 1)
        faulty = 1 / (abs(v2[1] - v1[1]) + 1)
        awards = 1 / (abs(v2[2] - v1[2]) + 1)
        login = 1 / (abs(v2[3] - v1[3]) + 1)

        return grade * 0.2 + faulty * 0.3 + awards * 0.4 + login * 0.1

    @staticmethod
    @udf(returnType=FloatType())
    def cal_team_sim(v1, v2, roles_vector1, roles_vector2, members_roles_vector1, members_roles_vector2, members_faculties_vector1, members_faculties_vector2):
        def linalg_sim(vec1, vec2):
            return 1.0 / float(1.0 + linalg.norm(np.array(vec1) - np.array(vec2)))

        competition_id = 1 if v1[0] == v2[0] else 0
        like_count = 1 / (abs(v2[1] - v1[1]) + 1)

        roles_sim = linalg_sim(roles_vector1, roles_vector2)
        members_roles_sim = linalg_sim(members_roles_vector1, members_roles_vector2)
        members_faculties_sim = linalg_sim(members_faculties_vector1, members_faculties_vector2)

        return competition_id * 0.3 + like_count * 0.1 + roles_sim * 0.4 + members_roles_sim * 0.1 + members_faculties_sim * 0.1

    def cal_user(self, user_vector):
        m = max([_[1] for _ in user_vector]) + 1

        df = self.sc.createDataFrame(data=user_vector, schema=["i", "j", "v1", "v2"])
        df = df.withColumn("sim", self.cal_user_sim(df.v1, df.v2))
        res = df.collect()

        # sim = np.zeros((m, m))
        #
        # for i in range(len(res)):
        #     sim[res[i]["i"]][res[i]["j"]] = res[i]["sim"]
        #     sim[res[i]["j"]][res[i]["i"]] = res[i]["sim"]

        return res

    def cal_team(self, team_vector):
        m = max([_[1] for _ in team_vector]) + 1

        df = self.sc.createDataFrame(data=team_vector, schema=["i", "j", "v1", "v2", "r1", "r2", "mr1", "mr2", "mf1", "mf2"])
        df = df.withColumn("sim", self.cal_team_sim(df.v1, df.v2, df.r1, df.r2, df.mr1, df.mr2, df.mf1, df.mf2))
        res = df.collect()

        # sim = np.zeros((m, m))
        #
        # for i in range(len(res)):
        #     sim[res[i]["i"]][res[i]["j"]] = res[i]["sim"]
        #     sim[res[i]["j"]][res[i]["i"]] = res[i]["sim"]

        return res

    def prepare_team_vector(self, team_data):
        total_roles = set()
        total_members_roles = set()
        total_members_faculties = set()

        for team in team_data:
            total_roles |= set(team.roles)
            total_members_roles |= team.members_roles
            total_members_faculties |= team.members_faculties

        total_roles = list(total_roles)
        total_members_roles = list(total_members_roles)
        total_members_faculties = list(total_members_faculties)

        for team in team_data:
            team.roles_vector = np.zeros(len(total_roles))
            team.members_roles_vector = np.zeros(len(total_members_roles))
            team.members_faculties_vector = np.zeros(len(total_members_faculties))

            for role in team.roles:
                team.roles_vector[total_roles.index(role)] = 1
            for role in team.members_roles:
                team.members_roles_vector[total_members_roles.index(role)] = 1
            for faculty in team.members_faculties:
                team.members_faculties_vector[total_members_faculties.index(faculty)] = 1

    def prepare_data(self, user_data, team_data):
        # rating_data
        rating_data = np.zeros((len(user_data), len(team_data)))
        rating_data.fill(np.nan)

        for i, user in enumerate(user_data):
            for j, team in enumerate(team_data):
                score = 0
                flag = False  # cold start data
                if team.id in user.interesting_team:
                    score += 3
                    flag = True
                if team.id in user.uninteresting_team:
                    score -= 15
                    flag = True
                if team.id in user.favorite_team:
                    score += 10
                    flag = True
                if team.competition_id in user.subscribe_competition:
                    score += 2
                    flag = True

                score += 2 * len([x for x in user.subscribe_role if x in team.roles])
                score -= len(team.members_roles & user.subscribe_role & set(team.roles))

                score += len(user.get_keywords() & team.get_keywords()) * 5

                if flag:
                    if score <= 0:
                        score = 0
                    elif score > 30:
                        score = 30

                    rating_data[i][j] = score

        user_vector = [user.get_vector() for user in user_data]

        self.prepare_team_vector(team_data)
        team_vector = [team.get_vector() for team in team_data]
        roles_vector = [team.roles_vector for team in team_data]
        members_roles_vector = [team.members_roles_vector for team in team_data]
        members_faculties_vector = [team.members_faculties_vector for team in team_data]

        user_vector_param = []
        for i in range(len(user_vector)):
            for j in range(len(user_vector)):
                if i < j:
                    user_vector_param.append((i, j, user_vector[i], user_vector[j]))

        team_vector_param = []
        for i in range(len(team_vector)):
            for j in range(len(team_vector)):
                if i < j:
                    team_vector_param.append((
                        i, j,
                        team_vector[i], team_vector[j],
                        roles_vector[i].tolist(), roles_vector[j].tolist(),
                        members_roles_vector[i].tolist(), members_roles_vector[j].tolist(),
                        members_faculties_vector[i].tolist(), members_faculties_vector[j].tolist()
                    ))

        return rating_data, user_vector_param, team_vector_param

    def fusion_recommend(self, user_data, team_data):
        rating_data, user_vector_param, team_vector_param = self.prepare_data(user_data, team_data)

        user_sim = self.cal_user(user_vector_param)
        team_sim = self.cal_team(team_vector_param)

        for item in user_sim:
            user1, user2 = item["i"], item["j"]
            for i in range(len(rating_data[user1])):
                if item["sim"] < 0.85:
                    continue
                if np.isnan(rating_data[user1][i]) and not np.isnan(rating_data[user2][i]):
                    rating_data[user1][i] = rating_data[user2][i] * item["sim"]
                elif np.isnan(rating_data[user2][i]) and not np.isnan(rating_data[user1][i]):
                    rating_data[user2][i] = rating_data[user1][i] * item["sim"]

        for item in team_sim:
            team1, team2 = item["i"], item["j"]
            for i in range(len(rating_data)):
                if item["sim"] < 0.75:
                    continue
                if np.isnan(rating_data[i][team1]) and not np.isnan(rating_data[i][team2]):
                    rating_data[i][team1] = rating_data[i][team2] * item["sim"]
                elif np.isnan(rating_data[i][team2]) and not np.isnan(rating_data[i][team1]):
                    rating_data[i][team2] = rating_data[i][team1] * item["sim"]

        model = self.train_als(rating_data)

        user_recommend = model.recommendForAllUsers(5).collect()
        user_matrix = {}

        for i in range(len(user_recommend)):
            user_index = user_recommend[i]["user"]
            user = user_data[user_index]
            user_matrix[user.id] = []
            for item in user_recommend[i][1]:
                user_matrix[user.id].append(team_data[item[0]].id)

        team_recommend = model.recommendForAllItems(50).collect()
        team_matrix = {}

        for i in range(len(team_recommend)):
            team_index = team_recommend[i]["item"]
            team = team_data[team_index]
            team_matrix[team.id] = []
            for item in team_recommend[i][1]:
                team_matrix[team.id].append(user_data[item[0]].id)

        return user_matrix, team_matrix
