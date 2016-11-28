$(document).ready(function() {
	$('#createuser').click(function() {
		var email = $('#email').val();
		var pass = $('#password').val();
		
		$.ajax({
			type: "POST",
			url: "../../SurveyBackend/src/user/createUser",
			data: { email, pass, first, last },
			success: function() {
				window.location.replace("http://stackoverflow.com");
			}
		});
	});
});