from django.http import HttpResponseRedirect
from django.shortcuts import render
from django.contrib import auth
from django.conf import settings
from django.contrib.auth.decorators import login_required

from .forms import SSUserCreationForm, SSUserChangeForm


def register(request):
    """
    """

    # Redirect to the logged-in page specified in the settings file.
    if request.user.is_authenticated():
        return HttpResponseRedirect(settings.LOGIN_REDIRECT_URL)

    if request.method == 'POST':
        form = SSUserCreationForm(request.POST)
        if form.is_valid():
            form.clean_email()
            username = form.clean_username()
            password = form.clean_password2()
            form.save()
            user = auth.authenticate(username=username, password=password)
            if user is not None:
                auth.login(request, user)
                return HttpResponseRedirect(
                    settings.LOGIN_REDIRECT_URL + '?brand_new_user')
    else:
        form = SSUserCreationForm()

    return render(request, "auth/register.html", {
        'form': form,
    })


@login_required
def user_change(request):
    """
    """
    if request.method == 'POST':
        user_form = SSUserChangeForm(
            request.POST, instance=request.user)

        if user_form.is_valid():
            new_user = user_form.save(commit=False)
            new_user.save()
            return HttpResponseRedirect("/overview/?info_updated")
    else:
        user_form = SSUserChangeForm(instance=request.user)

    return render(request, "auth/user_change.html", {
        'user_form': user_form,
    })
