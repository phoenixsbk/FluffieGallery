var getAllWeeks = function() {
  $.ajax({
    url: '/week/all',
    dataType: 'json',
    success: function(data) {
      if (data && data.length > 0) {
        $.each(data, function(i, w) {
          $('#weekcontainer').append(createBlock(w));
        });
      }
    },
    error: function(err) {
    }
  });
};

var createBlock = function(week) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-xl-6 no-padding'
  });

  var contentdiv = $('<div>', {
    'class': 'blog-content flex'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>');
  var figimg = $('<img>', {
    'src': '/weekphoto/' + week.photoPath
  });
  figure.append(figimg);
  contentdiv.append(figure);

  var linkdiv = $('<div>', {
    'class': 'entry-content flex flex-column justify-content-between align-items-start'
  });
  contentdiv.append(linkdiv);

  var headerline = $('<header>');

  var hline = $('<h4>');
  var linkline = $('<a>', {
    'href': '/week.html?id=' + week.id
  });
  linkline.append(week.name);
  hline.append(linkline);
  headerline.append(hline);
  linkdiv.append(headerline);

  var footerline = $('<footer>', {
    'class': 'flex flex-wrap align-items-center'
  });

  var dateline = $('<div>', {
    'class': 'posted-on'
  });
  dateline.append(week.startDate);
  footerline.append(dateline);

  /*var ulline = $('<ul>', {
    'class': 'flex flex-wrap align-items-center'
  });

  var liline = $('<li>');
  var lia = $('<a>', {
    'href': '/week.html?id=' + week.id
  });
  liline.append(lia);
  lia.append(week.startDate);

  ulline.append(liline);
  footerline.append(ulline);*/
  linkdiv.append(footerline);

  return paddiv;
}

$(function() {
  getAllWeeks();
})