from django.shortcuts import render,HttpResponse,get_object_or_404
from api.models import *
import json
from django.core import serializers

import decimal

def index(request):
	return HttpResponse("Hello, world. You're at the polls index.")

def findBySubString(request):
	text = request.GET.get('text')
	products = Product.objects.filter(name__contains=text)
	data = []
	for product in products:
		product_data = {}
		product_data['name'] = product.name
		product_data['url'] = product.url
		product_data['id'] = product.barcode
		data.append(product_data.copy())

	return HttpResponse(json.dumps(data,ensure_ascii=False).encode('utf8'), content_type='application/json; charset=utf-8')


def find(request):
	barcode = request.GET.get('id')	
	branches = Branch.objects.filter(merchandises__barcode__exact=barcode)
	data = []
	for branch in branches:
		branch_product_branch = branch.product_branch_set.get(branch=branch)
		price = branch_product_branch.price
		# print branch_product_branch.price
		store_name = branch.store.name

		bpb_data = {}
		bpb_data['branch_name'] = branch.name
		bpb_data['address'] = branch.address
		bpb_data['latitude'] = branch.lat
		bpb_data['longitude'] = branch.lon
		bpb_data['price'] = price
		bpb_data['store_name'] = store_name

		data.append(bpb_data.copy())


	def decimal_default(obj):
		if isinstance(obj, decimal.Decimal):
			return float(obj)
		raise TypeError
	
	# print data
	return HttpResponse(json.dumps(data, default=decimal_default, ensure_ascii=False).encode('utf8'), content_type = "application/json; charset=utf-8")
	
