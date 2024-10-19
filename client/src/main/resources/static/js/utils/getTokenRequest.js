async function getTokenRequest() {
    const response = await $.ajax({
        url: '/token',
        method: 'GET',
        dataType: 'json'
    });
    return response.token; // Return global token.
}