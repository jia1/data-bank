$(document).ready(function() {
    $.ajax({
        url: '',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: (res) => {
            // Populate table
        },
        error: (xhr, status, error) => {
            alert(error);
        }
    });
    event.preventDefault();
});
