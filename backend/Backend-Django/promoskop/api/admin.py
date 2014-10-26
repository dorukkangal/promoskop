from django.contrib import admin
from api.models import *

# Register your models here.
admin.site.register(Product)
admin.site.register(Store)
admin.site.register(Branch)
admin.site.register(Product_Branch)