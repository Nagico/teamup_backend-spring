import jieba.analyse
import json


class User:
    def __init__(self, data=None):
        self.vector = None
        self.keywords = None
        if data is None:
            self.id = None
            self.student_id = None
            self.introduction = None
            self.awards = None
            self.last_login = None
            self.subscribe_competition = None
            self.subscribe_role = None
            self.favorite_team = None
            self.interesting_team = None
            self.uninteresting_team = None
        else:
            self.id = int(data[0])
            self.student_id = data[1]
            self.introduction = data[2]
            self.awards = self.load_json(data[3])
            self.last_login = data[4]
            self.subscribe_competition = set(self.load_json(data[5]))
            self.subscribe_role = set(self.load_json(data[6]))
            self.favorite_team = set(self.load_json(data[7]))
            self.interesting_team = set(self.load_json(data[8]))
            self.uninteresting_team = set(self.load_json(data[9]))

    @staticmethod
    def load_json(data):
        return json.loads(data if data is not None else '[]')

    def get_vector(self):
        if self.vector is None:
            if len(self.student_id) < 8:
                pass
            self.vector = [
                (int((self.student_id[0:4])) - 2018),
                (int((self.student_id[4:8])) - 3000),
                len(self.awards),
                int(self.last_login.strftime("%Y%m%d%H%M%S") if self.last_login is not None else 0),
            ]
        return self.vector

    def get_keywords(self):
        if self.keywords is None:
            award_key_list = []

            for award in self.awards:
                award_key_list += jieba.analyse.extract_tags(award, topK=5)

            award_keys = set()
            for key in award_key_list:
                if not key.endswith('级') and not key.endswith('等奖') and not key.endswith('等') and key not in ['奖学金',
                                                                                                                  '竞赛']:
                    award_keys.add(key)

            self.keywords = award_keys

        return self.keywords
