$(document).ready(function () {
    var events = $('.event');
    events.each(function (index, element) {
        var evdate = $(element).data('eventdate');
        var classdate = '.' + evdate;

        var checkboxDate = $('#cb' + evdate);
        checkboxDate.attr('disabled', 'true');
        checkboxDate.parent().addClass('occupiedDay')
        checkboxDate.parent().on('mouseenter', function () {
            $(classdate).addClass('highlight');
        });
        checkboxDate.parent().on('mouseleave', function () {
            $(classdate).removeClass('highlight');
        });
        $(element).on('mouseenter', function () {
            $(classdate).addClass('highlight');
        });
        $(element).on('mouseleave', function () {
            $(classdate).removeClass('highlight');
        });

    })


});