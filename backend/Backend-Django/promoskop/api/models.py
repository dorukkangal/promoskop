from django.db import models

class Product(models.Model):
	name = models.CharField(max_length=255)
	url = models.CharField(max_length=255)
	barcode = models.IntegerField()

	def __unicode__(self):
		return self.name


class Store(models.Model):
	name = models.CharField(max_length=50)

	def __unicode__(self):
		return self.name

class Branch(models.Model):
	name = models.CharField(max_length=100)
	address = models.CharField(max_length=255)
	lat = models.FloatField()
	lon = models.FloatField()
	store = models.ForeignKey(Store)
	merchandises = models.ManyToManyField(Product, through='Product_Branch')

	def __unicode__(self):
		return self.name

class Product_Branch(models.Model):
	product = models.ForeignKey(Product)
	branch = models.ForeignKey(Branch)
	price = models.DecimalField(max_digits=19, decimal_places=2)
