from django.http import HttpResponse, JsonResponse, HttpResponseRedirect
from django.template import loader
from django.db import connection
from chat.message.message import get_message
from chat.message.message import create_message
from chat.message.message import up_message
from chat.message.message import del_message


def index(request):
    template = loader.get_template('chat/index.html')
    return HttpResponse(template.render())

def message(request):
    if request.method == 'GET':
        last = request.GET.get('last', '0')
        data = get_message(last)
        return JsonResponse(data, safe=False)
    if request.method == 'POST':
        create_message(request.POST.get('user', ''), request.POST.get('text', ''))
        return HttpResponse('')

def update_message(request, id):
    if request.method == 'POST':
        up_message(id, request.POST.get('text', ''))
    return HttpResponse('')

def delete_message(request, id):
    if request.method == 'POST':
        del_message(id)
    return HttpResponse('')