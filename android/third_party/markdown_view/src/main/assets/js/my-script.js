$(document).ready(function () {

    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollup').css('visibility', 'visible');
        } else {
            $('.scrollup').css('visibility', 'hidden');
        }
    });

    $('.scrollup').click(function () {
        $("html, body").animate({
            scrollTop: 0
        }, 1200);
        return false;
    });
});