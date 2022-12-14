from Database import Database
from Spark import Spark


if __name__ == '__main__':
    db = Database()
    spark = Spark()

    user_data, team_data = db.fetch_data()

    user_matrix, team_matrix = spark.fusion_recommend(user_data, team_data)

    db.update_data(user_matrix, team_matrix)



