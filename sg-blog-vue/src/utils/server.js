
//初始化时间
const initDate = (oldDate,full) => {
    var odate = new Date(oldDate);
    var year =  odate.getFullYear();
    var month = odate.getMonth()<9? '0' + (odate.getMonth()+1) : odate.getMonth()+1;
    var date = odate.getDate()<10? '0'+odate.getDate() : odate.getDate();
    var hour = odate.getHours()<10? '0'+odate.getHours() : odate.getHours();
    var min = odate.getMinutes()<10? '0'+odate.getMinutes() : odate.getMinutes();
    if(full=='all'){
        var t = oldDate.split(" ")[0];
        // console.log(oldDate,t.split('-')[0],t.split('-')[1],t.split('-')[2]);
        var h = oldDate.split(" ")[1];
        return t.split('-')[0]+'年'+t.split('-')[1]+'月'+t.split('-')[2]+'日' + h.split(':')[0] +":"+h.split(':')[1];
    }else if(full=='year'){
        return year
    }else if(full== 'month'){
        return odate.getMonth()+1
    }else if(full == 'date'){
        return date
    }else if(full== 'newDate'){
        return year+'年'+month+'月'+date+'日';
    }
}



export {
        initDate//设置时间

    }
