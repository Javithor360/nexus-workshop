$(document).ready(function () {
    // Add class 'has-value' if the input or textarea had value after loading
    $('.form-input input, .form-input textarea').each(function () {
        if ($(this).val().length > 0) {
            $(this).parent().addClass('has-value');
        }
    });

    // Listen on inputs to add or remove the class 'has-value'
    $('.form-input input, .form-input textarea').on('focus input', function () {
        if ($(this).val().length > 0){
            $(this).parent().addClass('has-value');
        } else {
            $(this).parent().removeClass('has-value');
        }
    });
});