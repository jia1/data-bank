$('form').submit(function(event) {
    let arr = $(this).serializeArray();
    let obj = {};
    for (let i = 0; i < arr.length; i++) {
        obj[arr[i]['name']] = arr[i]['value'];
    }
    /*
    $.ajax({
        url: '',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: obj,
        success: (res) => {
            alert('Your response has been recorded.');
        },
        error: (xhr, status, error) => {
            alert(error);
        }
    });
    */
    alert('Your response has been recorded.');
    location.reload();
    event.preventDefault();
});
