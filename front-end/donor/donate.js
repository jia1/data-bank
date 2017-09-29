const baseUrl = '';

$('form').submit(function(event) {
    let arr = $(this).serializeArray();
    let obj = {donorId: '1'};
    for (let i = 0; i < arr.length; i++) {
        obj[arr[i]['name']] = arr[i]['value'];
    }
    $.ajax({
        url: '/donor/insert',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: obj,
        success: (res) => {
            alert('Your response has been recorded.');
            location.reload();
        },
        error: (xhr, status, error) => {
            alert(error);
        }
    });
    event.preventDefault();
});
