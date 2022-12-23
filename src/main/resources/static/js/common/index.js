let postAjax = (url, data) => {
    return new Promise((resolve, reject) => { 
     $.ajax({
            type: "POST",
            url,
            data,
         success: (data) => {
             if (data.code == "200") {
                resolve(data);
             } else { 
                reject();
             }

            },
            error : (error) => {
                alert("Error!");
                reject();
            }
        })
    })
}