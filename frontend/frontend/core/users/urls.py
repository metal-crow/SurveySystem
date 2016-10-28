from django.conf.urls import patterns
from django.views.generic import TemplateView
from django.contrib.auth.views import login
from django.contrib.auth.decorators import user_passes_test
from django.conf import settings


# if the user is not logged in, then allow them to use the login form,
# otherwise redirect them to the LOGIN_REDIRECT_URL
not_logged_in = user_passes_test(lambda u: u.is_anonymous(),
                                 settings.LOGIN_REDIRECT_URL)

urlpatterns = patterns(
    '',

    # a page for potential users to register for SurveySystem
    (r'^signup/$', 'frontend.core.users.views.register'),

    # a login page for registered users to access their previously
    # created SurveySystem account
    (r'^login/$', not_logged_in(login), {
        'template_name': 'auth/login.html'}),

    # a logout URL that logs users out of SurveySystem
    (r'^logout/$', 'django.contrib.auth.views.logout', {
        'next_page': settings.LOGIN_URL}),

    # a page with a form for users to edit their account preferences
    (r'^user-change/$', 'frontend.core.users.views.user_change'),
)
