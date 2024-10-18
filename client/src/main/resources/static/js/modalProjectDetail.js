$(document).ready(function() {
    // Open the modal
    $('#openModalBtn').click(function() {
        $('#modal').removeClass('hidden');
    });

    // Close the modal
    $('#closeModalBtn').click(function() {
        $('#modal').addClass('hidden');
    });

    // Close modal when clicking outside of it
    $('#modal').click(function(event) {
        if ($(event.target).is('#modal')) {
            $(this).addClass('hidden');
        }
    });
});