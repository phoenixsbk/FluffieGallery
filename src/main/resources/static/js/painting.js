var getPainting = function(id) {
  $.ajax({
    url: '/painting/paintingid/' + id,
    dataType: 'json',
    success: function(data) {
      var weekname = data.week.name;
      var filepath = data.filePath;
      $('#paintingimg').attr('src', '/gallery/week_' + weekname + '/' + filepath);
      $('#paintingdesc').text(data.description);

      if (data.comments && data.comments.length > 0) {
        $('#innercmtcontainer').empty();
        $.each(data.comments, function(i, c) {
          $('#innercmtcontainer').append(createComment(c))
        });
      }
    },
    error: function(err) {
    }
  });
};

var createComment = function(comment) {
  var licontainer = $('<li>', {
    'class': 'comment'
  });

  var article = $('<article>', {
    'class': 'comment-body'
  });
  licontainer.append(article);

  var fig = $('<figure>', {
    'class': 'comment-author-avatar'
  });
  article.append(fig);

  var figimg = $('<img>', {
    'src': '/avatar/' + comment.commenter.avatar,
    'alt': comment.commenter.name
  });
  fig.append(figimg);

  var wrap = $('<div>', {
    'class': 'comment-wrap'
   });
   article.append(wrap);

   var authorline = $('<div>', {
    'class': 'comment-author'
   });
   wrap.append(authorline);

   var spanline = $('<span>', {
    'class': 'fn'
   });
   authorline.append(spanline);
   spanline.append(comment.commenter.name);

   var dateline = $('<span>', {
    'class': 'comment-meta'
   });
   authorline.append(dateline);
   dateline.append(',' + comment.date);

   var pline = $('<p>');
   pline.append(comment.text);
   wrap.append(pline);

   var clearline = $('<div>', {
    'class': 'clearfix'
   });
   article.append(clearline);

   return licontainer;
};

var addComment = function() {
  var cmt = $('#addCmtText').val();
  if (cmt) {
    var paintingid = $('#curpaintingid').val();
    var payload = {
      'paintingId': paintingid,
      'comment': cmt
    };
    $.ajax({
      url: '/comment/add',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(payload),
      dataType: 'text',
      success: function(resp) {
        $('#addCmtText').val('');
        getPainting(paintingid);
      },
      error: function(jqr, status, err) {
        if (400 === jqr.status) {
          alert('wrong parameter');
        }
      }
    });
  }
};

$(function() {
  $('#addCmt').on('click', addComment);
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams) {
    var paintingid = urlParams.get('id');
    if (paintingid) {
      $('#curpaintingid').val(paintingid);
      getPainting(paintingid);
    }
  }
})