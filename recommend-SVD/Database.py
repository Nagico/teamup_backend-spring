import json
import os
import pymysql

from entity.Team import Team
from entity.User import User


class DatabaseConfig:
    host = os.environ.get('MYSQL_HOST', 'localhost')
    port = os.environ.get('MYSQL_PORT', 3306)
    user = os.environ.get('MYSQL_USER', 'zq_teamup')
    password = os.environ.get('MYSQL_PASSWORD', '')
    database = os.environ.get('MYSQL_DATABASE', 'zq_teamup')


class Database:
    def __init__(self):
        self.db = pymysql.connect(
            host=DatabaseConfig.host,
            port=DatabaseConfig.port,
            user=DatabaseConfig.user,
            password=DatabaseConfig.password,
            database=DatabaseConfig.database,
        )
        self.cursor = self.db.cursor()

    def __del__(self):
        self.db.close()

    def execute(self, sql):
        print(sql)
        self.cursor.execute(sql)
        self.db.commit()

    def query(self, sql):
        self.cursor.execute(sql)
        return self.cursor.fetchall()

    def fetch_data(self):
        # User data
        users = self.query('SELECT id, student_id, introduction, '
                           'awards, last_login, subscribe_competition, '
                           'subscribe_role, favorite_team, interesting_team, '
                           'uninteresting_team '
                           'FROM user')
        user_data = [User(data) for data in users]

        # Team data
        teams = self.query('SELECT team.id, competition_id, '
                           'competition.name, competition.abbreviation, '
                           'interesting_count, members, roles '
                           'FROM team, competition '
                           'WHERE team.competition_id = competition.id')
        team_data = [Team(data) for data in teams]

        return user_data, team_data

    def update_data(self, user_data, team_data):
        self.cursor.execute("DELETE FROM recommend")
        cnt = 0
        for user_id, items in user_data.items():
            cnt += 1
            self.cursor.execute(f"INSERT INTO recommend (`id`, `items`, `object_id`, `type`) VALUES ({cnt}, '{json.dumps(items)}', {user_id}, 0)")

        for team_id, items in team_data.items():
            cnt += 1
            self.cursor.execute(f"INSERT INTO recommend (`id`, `items`, `object_id`, `type`) VALUES ({cnt}, '{json.dumps(items)}', {team_id}, 1)")

        self.db.commit()
