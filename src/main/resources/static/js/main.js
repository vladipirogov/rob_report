var mainTable = null;

fillTable = (data) => {
    var rowObject = rowDto(data);
    var result = [];

    Object.keys(rowObject).forEach(function(key, index) {
        result.push(rowObject[key]);
    });
    return result;
}

function rowDto(report) {
   var result = {
       routeId:report.routeId,
       dateStart: report.dateStart != null ? moment(report.dateStart).format('YYYY-MM-DD HH:mm:ss') : '',
       dateEnd: report.dateEnd != null ? moment(report.dateEnd).format('YYYY-MM-DD HH:mm:ss') : '',
       timeWork:report.timeWork,
       name:report.name,
       hrs:report.hrs,
       avrLoad:report.avrLoad,
       prf:report.prf,
       alarm:report.alarm,
       pal:report.pal,
       tMax:report.tmax,
       tBeltMax:report.tbeltMax
   };
   return result;
}

request = (url, value, method, fun) => {
    var payload = { method: method,
        mode: 'cors',
        cache: 'default',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'},
        body:JSON.stringify(value)
    };
    fetch(url, payload)
        .then(response => response.json()) // Result from the
        .then(data => {
           fun(data);
        })
        .catch(error => console.log(error))
}



function Update() {

    var tableDataSet = null;

    dataSet : tableDataSet;

    fillRows = (data) => {
        data.forEach(function (element) {
            tableDataSet.push(fillTable(element)) // Prints result from `response.json()`
        })
        mainTable.rows.add(tableDataSet).draw();
    }

this.findByPeriod = () => {
        mainTable.clear();
        mainTable.draw();
        tableDataSet = [];
        var checkBox = document.getElementById("check_end_date").checked;
        var input = document.getElementById('report-date-input').value
        var input_end = document.getElementById('report-date-end').value
        var dateInput = moment(input, 'YYYY-MM-DD HH:mm:ss').utc(-120).format();
        var endDateInput = checkBox == true ? moment(input_end, 'YYYY-MM-DD HH:mm:ss').utc(-120).format() : moment(input, 'YYYY-MM-DD HH:mm:ss').add(1, 'days').utc(-120).format();
        var value = {dateInput : dateInput, endDateInput:endDateInput};
        request('/find_updates', value, 'POST', fillRows)
    }

}

var updates = new Update();

$(document).ready(function() {
  mainTable = $('#reports_table').DataTable({
         "pageLength": 25,
         "ordering": false,
         language: {
                 processing:     "Подождите...",
                 search:         "Поиск:",
                 lengthMenu:    "Показать _MENU_ записей",
                 info:           "Записи с _START_ до _END_ из _TOTAL_ записей",
                 infoEmpty:      "Записи с 0 до 0 из 0 записей",
                 infoFiltered:   "(отфильтровано из _MAX_ записей)",
                 infoPostFix:    "",
                 loadingRecords: "Загрузка записей...",
                 zeroRecords:    "Записи отсутствуют.",
                 emptyTable:     "В таблице отсутствуют данные",
                 paginate: {
                     first:      "Первая",
                     previous:   "Предыдущая",
                     next:       "Следующая",
                     last:       "Последняя"
                 },
                 aria: {
                     sortAscending:  ": активировать для сортировки столбца по возрастанию",
                     sortDescending: ": активировать для сортировки столбца по убыванию"
                 }
             }
    });
    $("#reports_table").removeClass( "dataTable" );
} );