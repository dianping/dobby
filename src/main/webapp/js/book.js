function show(id) {
	var cell = document.getElementById(id);

	if (cell.style.display == 'none') {
		cell.style.display = 'block';
	} else {
		cell.style.display = 'none';
	}
}
