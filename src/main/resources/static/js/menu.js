
$(function() {
    $( '.hamburger-menu' ).on( 'click', function() {
        $(this).toggleClass('close');
        $('.site-branding').toggleClass('hide');
        $('.site-navigation').toggleClass('show');
        $('.site-header').toggleClass('no-shadow');
    });
});
