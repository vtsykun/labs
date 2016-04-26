from django.http import HttpResponse
from django.template import loader
from chat.settings import *


def index(request):
    template = loader.get_template('chat/index.html')
    return HttpResponse(template.render())