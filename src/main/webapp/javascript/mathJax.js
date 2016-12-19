function mathJaxFunc(){
    if($('#useMathJax').prop('checked')){
        $('head').append('<script async src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML"> </script>');
    } else {
        location.reload();
    }
}