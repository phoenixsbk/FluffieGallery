var getWeek = function(id) {
  $.ajax({
    url: '/week/weekid/' + id,
    dataType: 'json',
    success: function(data) {
      $('#weekimage').attr('src', '/weekphoto/' + data.photoPath);
      $('#weekname').text(data.name);
      $('#crumbweek').text(data.name);
      $('#weekdate').text(data.startDate);
      if (data.paintings && data.paintings.length > 0) {
        $.each(data.paintings, function(i , p) {
          $('#weekimgcontainer').append(createBlock(data.name, p));
        });

        $('figure').fancybox({
          toolbar: true,
          smallBtn: true,
          thumbs: {
            axis: 'x'
          },
          iframe: {
            css: {
              maxwidth: '80%',
              maxheight: '80%',
              margin: 0,
              width: '70%',
              height: '80%'
            }
          }
        });
      }
    }
  });
};

var createBlock = function(weekname, painting) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-md-3 col-lg-2'
  });

  var contentdiv = $('<div>', {
    'class': 'portfolio-content'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>', {
    'class': 'img-sm-auto',
    'data-fancybox': 'gallery',
    'data-type': 'iframe',
    'data-src': '/painting.html?id=' + painting.id
  });
  var figimg = $('<img>', {
    'src': '/gallery/week_' + weekname + '/' + painting.filePath,
    'class': 'img-center'
  });
  figure.append(figimg);
  contentdiv.append(figure);
  return paddiv;
};

$(function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams) {
    var weekid = urlParams.get('id');
    if (weekid) {
      getWeek(weekid);
    }
  }
})