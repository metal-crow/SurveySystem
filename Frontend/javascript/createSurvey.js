$(document).ready(function() {
	$('#createSurvey').click(function() {
		var em = $('#email').val();
		var pass = $('#password').val();
		
		function setCookie(em, pass, exdays) {
			var d = new Date();
			d.setTime(d.getTime() + (exdays*24*60*60*1000));
			var expires = "expires="+ d.toUTCString();
			document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
		}
		function getCookie(cname) {
			var name = cname + "=";
			var ca = document.cookie.split(';');
			for(var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ') {
					c = c.substring(1);
				}
				if (c.indexOf(name) == 0) {
					return c.substring(name.length, c.length);
				}
			}
			return "";
		}
		function checkCookie() {
			var ema=getCookie("email");
			if (ema!="") {
				continue;
			} else {
				alert("you're not logged in")
				ema = prompt("Please enter your email:", "");
				pswd = prompt("pease enter your password", "")
				if (ema != "" && ema != null) {
					if (pswd != "" && pswd != null){
						setCookie("user_id", ema + pswd, 365);
					}
				}
			}
		}
		
		var hash = 0, i, chr, len;
		if (this.length === 0) return hash;
		for (i = 0, len = this.length; i < len; i++) {
			chr   = this.charCodeAt(i);
			hash  = ((hash << 5) - hash) + chr;
			hash |= 0;
		}
		
		$.ajax({
			type: "POST",
			url: "http://localhost:4567/createUser",
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