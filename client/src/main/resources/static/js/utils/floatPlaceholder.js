$(document).ready(function () {
    // Add class 'has-value' if the input had value after loading
    $('.form-input input').each(function () {
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
    $('.form-input select').on('focus input', function () {
        if($(this).prop('selected', true)){
            $(this).parent().addClass('has-value');
        }
    });
});