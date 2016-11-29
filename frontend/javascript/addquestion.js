var counter = 1;

function addQuestion(input) {
	var newQ = document.createElement('div');
	newQ.innerHTML = "Question " + (counter + 1) + "<br><input type='text' name='questions[]'>";
	document.getElementById(input).appendChild(newQ);
	counter++;
}