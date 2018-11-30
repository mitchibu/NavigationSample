# -*- coding: utf-8 -*-
import json
from collections import OrderedDict
import pprint
import javapoet

class Type:
	name = ''
	isNonNull = False
	isList = False

	def __init__(self, name, isNonNull, isList):
		self.name = name
		self.isNonNull = isNonNull
		self.isList = isList

def findType(json, isNonNull, isList):
	if json['ofType'] is None:
		return Type(json['name'], isNonNull, isList)
	else:
		isNonNull = True if isNonNull else json['kind'] == 'NON_NULL'
		isList = True if isList else json['kind'] == 'LIST'
		return findType(json['ofType'], isNonNull, isList)

with open('scheme.json', 'r', encoding = "utf-8") as f:
	df = json.load(f)

for type in df['data']['__schema']['types']:
	name = type['name']
	kind = type['kind']
	if (kind == 'OBJECT') or (kind == 'INTERFACE'):
		writer = javapoet.Writer('jp.test', name)
		classBuilder = javapoet.ClassBuilder(name)\
			.addModifier(javapoet.Modifier.Public)\
			.addComment(type['description'])

		if type['fields'] is not None:
			for field in type['fields']:
				r = findType(field['type'], False, False)
				fieldType = 'List<' + r.name + '> ' if r.isList else r.name
				fieldBuilder = javapoet.FieldBuilder(fieldType, field['name'])\
					.addModifier(javapoet.Modifier.Public)\
					.addComment(field['description'])
				if r.isNonNull:
					fieldBuilder.addModifier(javapoet.Modifier.NonNull)
				classBuilder.addField(fieldBuilder)

		writer.output(classBuilder)
		writer.close()
	# print(name + ' ' + kind)
	# if type['fields'] is not None:
	# 	for field in type['fields']:
	# 		r = findType2(field['type'], False, False)
	# 		print('  ' + field['name'] + ' ' + r.name)
	#print(javapoet.FieldBuilder('String', 'test').addModifier(javapoet.Modifier.Public).build())
