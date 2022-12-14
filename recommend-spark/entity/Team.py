import jieba.analyse
import json


class Team:
    def __init__(self, data=None):
        self.keywords = None
        if data is None:
            self.id = None
            self.competition_id = None
            self.competition_name = None
            self.competition_abbreviation = None
            self.like_count = None
            self.members = []
            self.roles = set()
        else:
            self.id = data[0]
            self.competition_id = int(data[1])
            self.competition_name = data[2]
            self.competition_abbreviation = data[3]
            self.like_count = int(data[4])
            self.members = self.load_json(data[5])
            self.roles = [_['id'] for _ in self.load_json(data[6])]

        if self.members is not None:
            self.members_roles = set()
            self.members_faculties = set()
            for member in self.members:
                for role in member['roles']:
                    self.members_roles.add(role['id'])
                    self.members_faculties.add(member['faculty'])

    @staticmethod
    def load_json(data):
        return json.loads(data if data is not None else '[]')

    def get_vector(self):
        return [
            self.competition_id,
            self.like_count,
        ]

    def get_keywords(self):
        if self.keywords is None:
            key_list = []

            key_list += jieba.analyse.extract_tags(self.competition_abbreviation, topK=3)
            key_list += jieba.analyse.extract_tags(self.competition_name, topK=3)

            self.keywords = set(key_list)

        return self.keywords
