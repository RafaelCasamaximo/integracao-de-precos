import unidecode
import re

class StringNormalizer:
    def __init__(self) -> None:

        pass

    def compareStrings(self, str1, str2) -> bool:
        str1withoutSpecialCharacters = re.sub('\W+','', str1)
        str2withoutSpecialCharacters = re.sub('\W+','', str2)
        str1lowerCase = str1withoutSpecialCharacters.lower()
        str2lowerCase = str2withoutSpecialCharacters.lower()
        str1unicode = unidecode.unidecode(str1lowerCase)
        str2unicode = unidecode.unidecode(str2lowerCase)
        return str1unicode == str2unicode
