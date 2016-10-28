from django.contrib.auth.forms import AuthenticationForm, UserCreationForm
from django.contrib.auth.forms import UserChangeForm
from django.utils.translation import ugettext_lazy as _
from django import forms
from django.forms import ValidationError
from django.core.validators import validate_slug

from .models import SSUser


class SSUserCreationForm(UserCreationForm):
    """
    """

    # Custom error messages that get display on the form
    error_messages = {
        'password_mismatch': "The passwords entered do not match",
        'duplicate_email': "This email address is already in use",
        'duplicate_username': "This username is already in use",
    }

    # User personal information
    first_name = forms.CharField(
        label=_("First name:"),
        error_messages={
            'required': "Please enter your first name.",
        })
    last_name = forms.CharField(
        label=_("Last name:"),
        error_messages={
            'required': "Please enter your last name.",
        })
    email = forms.CharField(
        label=_("Email address:"),
        error_messages={
            'required': "Please enter your email address.",
        })

    # User account information
    username = forms.CharField(
        label=_("Username"),
        max_length=30,
        validators=[validate_slug],
        help_text=_("Required. 30 characters or fewer. Letters, digits, "
                    "underscores, and hyphens only."),
        error_messages={
            'invalid': _("This value may contain only letters, digits, "
                         "underscores, and hyphens"),
            'required': "Please enter your username.",
        })
    password1 = forms.CharField(
        label=_("Password:"),
        widget=forms.PasswordInput,
        error_messages={
            'required': "Please enter a password.",
        })
    password2 = forms.CharField(
        label=_("Password confirmation:"),
        widget=forms.PasswordInput,
        help_text=_("Enter the same password as above, for verification."),
        error_messages={
            'required': "Please confirm your password.",
        })

    class Meta:
        """
        """
        model = SSUser
        fields = (
            "first_name",
            "last_name",
            "email",
            "username",
            "password1",
            "password2",)

    def clean_username(self):
        # Since User.username is unique, this check is redundant,
        # but it sets a nicer error message than the ORM. See #13147.
        username = self.cleaned_data["username"]
        try:
            TSUser._default_manager.get(username=username)
        except TSUser.DoesNotExist:
            return username
        raise ValidationError(self.error_messages['duplicate_username'])

    def clean_email(self):
        # Since User.email is unique, this check is redundant,
        # but it sets a nicer error message than the ORM. See #13147.
        email = self.cleaned_data["email"]
        try:
            TSUser._default_manager.get(email=email)
        except TSUser.DoesNotExist:
            return email
        raise ValidationError(self.error_messages['duplicate_email'])

    def clean_password2(self):
        password1 = self.cleaned_data.get("password1")
        password2 = self.cleaned_data.get("password2")
        if password1 and password2 and password1 != password2:
            raise ValidationError(
                self.error_messages['password_mismatch'])
        return password2

    def save(self, commit=True):
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data["password1"])

        user.save()
        return user


class SSUserChangeForm(UserChangeForm):
    # Custom error messages that get display on the form
    error_messages = {
        'password_mismatch': "The passwords entered do not match",
        'duplicate_username': "This username is already in use",
    }

    # User personal information
    first_name = forms.CharField(
        label=_("First name:"),
        error_messages={
            'required': "Please enter your first name.",
        })
    last_name = forms.CharField(
        label=_("Last name:"),
        error_messages={
            'required': "Please enter your last name.",
        })
    email = forms.CharField(
        label=_("Email address:"),
        error_messages={
            'required': "Please enter your email address.",
        })

    class Meta:
        """
        """
        model = SSUser
        fields = (
            "first_name",
            "last_name",
            "email",)

    def __init__(self, *args, **kwargs):
        super(TSUserChangeForm, self).__init__(*args, **kwargs)
        self.fields.pop('username')
        self.fields.pop('password')
