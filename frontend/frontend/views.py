from django.shortcuts import render
from django.db.models import Q, Count
from django.http import HttpResponseRedirect

from toolshare import settings
from toolshare.core.users.models import SSUser


def landing(request):
    """
    """
    if request.user.is_authenticated():
        return HttpResponseRedirect(settings.LOGIN_REDIRECT_URL)
    else:
        return render(request, "landing.html")


def ss_overview(request):
    """
    The overview page displays the user's information and preferences
    as well as some community statistics
    """
    if not request.user.is_authenticated():
        return HttpResponseRedirect(settings.LOGIN_URL+'?next='+request.path)

