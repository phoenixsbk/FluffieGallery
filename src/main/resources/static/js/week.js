var getWeek = function(id) {
  $.ajax({
    url: '/week/weekid/' + id,
    dataType: 'json',
    success: function(data) {
      $('#weekimage').attr('src', '/weekphoto/' + data.photoPath);
      $('#weekname').text(data.name);
      $('#weekdate').text(data.startDate);
      if (data.paintings && data.paintings.length > 0) {
        $.each(data.paintings, function(i , p) {
          $('#weekimgcontainer').append(createBlock(data.name, p));
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
    'class': 'img-sm-auto'
  });
  var figimg = $('<img>', {
    'src': '/gallery/week_' + weekname + '/' + painting.filePath,
    'class': 'img-center'
  });
  figure.append(figimg);
  contentdiv.append(figure);

  var linkdiv = $('<div>', {
    'class': 'entry-content flex flex-column align-items-center justify-content-center'
  });
  contentdiv.append(linkdiv);

  var hline = $('<h5>');
  var linkline = $('<a>', {
    'href': '/painting.html?id=' + painting.id
  });
  linkline.append(painting.name);
  hline.append(linkline);
  linkdiv.append(hline);

  var ulline = $('<ul>', {
    'class': 'flex flex-wrap justify-content-center'
  });

  var liline = $('<li>');
  var lia = $('<a>', {
    'href': '/painting.html?id=' + painting.id
  });
  liline.append(lia);
  lia.append(painting.painter.name);

  ulline.append(liline);
  linkdiv.append(ulline);

  return paddiv;
}

$(function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams) {
    var weekid = urlParams.get('id');
    if (weekid) {
      getWeek(weekid);
    }
  }
})