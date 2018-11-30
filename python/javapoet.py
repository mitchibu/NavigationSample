import os
from enum import Enum

class Modifier(Enum):
	Final = 0
	Public = 1
	Private = 2
	Protected = 3
	NonNull = 4

class FieldBuilder:
	def __init__(self, type, name):
		self.type = type
		self.name = name
		self.modifier = []

	def addModifier(self, modifier):
		self.modifier.append(modifier)
		return self

	def addComment(self, comment):
		self.comment = comment
		return self

	def build(self):
		s = ''

		if Modifier.NonNull in self.modifier:
			s += (' ' if len(s) > 0 else '') + '@NonNull'

		if Modifier.Public in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'public'
		elif Modifier.Private in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'private'
		elif Modifier.Protected in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'protected'

		if Modifier.Final in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'final'

		s += (' ' if len(s) > 0 else '') + self.type
		s += (' ' if len(s) > 0 else '') + self.name
		s += ';'
	
		if self.comment is not None:
			s = '\t// ' + self.comment + '\n\t' + s
		return s + '\n'

class ClassBuilder:
	def __init__(self, name):
		self.name = name
		self.modifier = []
		self.fields = []

	def addModifier(self, modifier):
		self.modifier.append(modifier)
		return self

	def addComment(self, comment):
		self.comment = comment
		return self

	def addField(self, field):
		self.fields.append(field)
		return self

	def build(self):
		s = ''

		if Modifier.Public in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'public'
		elif Modifier.Private in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'private'
		elif Modifier.Protected in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'protected'

		if Modifier.Final in self.modifier:
			s += (' ' if len(s) > 0 else '') + 'final'

		s += (' ' if len(s) > 0 else '') + 'class ' + self.name + ' {\n'

		for field in self.fields:
			s += field.build()

		s += '}'

		if self.comment is not None:
			s = '// ' + self.comment + '\n' + s
		return s + '\n'

class Writer:
	def __init__(self, packageName, className):
		dir = packageName.replace('.', '/');
		os.makedirs(dir, exist_ok = True)
		self.fd = open(dir + '/' + className + '.java', 'w')
		self.packageName = packageName;

	def close(self):
		self.fd.close()

	def output(self, classBuilder):
		self.fd.write('package ' + self.packageName + ';\n\n')
		self.fd.write(classBuilder.build())
