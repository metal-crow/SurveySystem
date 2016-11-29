$(document).ready(function() {
	$('#loginuser').click(function() {
		var email = $('#email').val();
		var pass = $('#password').val();
		
		var hash = 0, i, chr, len;
		if (this.length === 0) return hash;
		for (i = 0, len = this.length; i < len; i++) {
			chr   = this.charCodeAt(i);
			hash  = ((hash << 5) - hash) + chr;
			hash |= 0;
		}
		
		$.ajax({
			type: "POST",
			url: "http://localhost:4567/login",
			data: JSON.stringify({ "email": em, "password": hash}),
			dataType: "json",
			success: function() {
				alert("true");
			},
			error: function(){
				alert("false");
			}
		});
	});
});