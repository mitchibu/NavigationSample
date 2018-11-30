import cssutils
import logging
cssutils.log.setLevel(logging.CRITICAL) 

fd = open('fa_string.xml', 'w')
fd.write('<?xml version="1.0" encoding="utf-8"?>\n')
fd.write('<resources>\n')

style = cssutils.parseFile('fontawesome.css')
for rule in style:
	if rule.type == rule.STYLE_RULE:
		property = rule.style.getProperty('content')
		if property is not None:
			name = rule.selectorText.strip('.').replace('-', '_').replace(':before', '')
			value = ascii(property.propertyValue[0].value).strip('\'').replace('\\u', '&#x') + ';'
			fd.write('\t<string name="' + name + '">' + value + '</string>\n')

fd.write('</resources>\n')
fd.close()
