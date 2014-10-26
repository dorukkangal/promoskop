from django.conf.urls import patterns, url

from api import views

urlpatterns = patterns('',

	# ex: /api/
    url(r'^$', views.index, name='index'),
    # ex: /api/products/findBySubString?text=U
    url(r'^products/findBySubString$', views.findBySubString, name='findBySubString'),
    # ex: /api/products/find?id=124155
    url(r'^products/find$', views.find, name='find'),
)