var url = 'http://localhost:8080';

var app = angular.module("myApp", ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'main.html',
            controller: "myCtrl"
        })
        .when('/etl', {
            templateUrl: 'etl.html',
            controller: "myCtrl"
        })
        .when('/produkty', {
            templateUrl: 'produkty.html',
            controller: "myCtrl"
        })
        .when('/opinie', {
            templateUrl: 'opinie.html',
            controller: "myCtrl"
        })
        .when('/baza', {
            templateUrl: 'czysc.html',
            controller: "myCtrl"
        });
}]);


app.controller("myCtrl", function($scope, $http, $location) {
    $scope.start = function() {
        $location.path("/");
    }
    
    $scope.extract = function() {
        $('#etlDiv').hide();
        $('#extractDiv').hide();
        $('#transformDiv').hide();
        $('#loadDiv').hide();
        
        var productCode = $scope.productCode;
        if (productCode == null || productCode == "") {
            $('#noCodeModal').modal('show');
        } else {
            $('#waitingModal').modal('show'); 
            $http.get(url + '/extract/' + productCode)
                .then(function successCallback(response) {
                    $('#waitingModal').modal('hide');
                    $('#extractDiv').show();
                    $scope.product = response.data.product;
                    $scope.pagesNumber = response.data.reviewsPagesNumber;
                    $scope.reviewsNumber = response.data.reviewsNumber;
                    $('#transform').attr('disabled', false);
                }, function errorCallback(response) {
                    $('#waitingModal').modal('hide');
                    $('#wrongCodeModal').modal('show');
                });
        }
    }
    
    $scope.transform = function() {
        $('#extractDiv').hide();
        $('#transform').attr('disabled', true);
        $('#load').attr('disabled', false);
        
        $('#waitingModal').modal('show');
        $http.get(url + '/transform')
            .then(function successCallback(response) {
                $('#transformDiv').show();
                $scope.productRecords = response.data.productRecords;
                $scope.reviewRecords = response.data.reviewRecords;
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                alert("Coś poszło nie tak");
            });
    }
    
    $scope.load = function() {
        $('#transformDiv').hide();
        $('#load').attr('disabled', true);
        $('#extract').attr('disabled', false);
        
        $('#waitingModal').modal('show');
        $http.get(url + '/load')
            .then(function successCallback(response) {
                $('#loadDiv').show();
                $scope.newProductsNumber = response.data.newProductRecordsNumber;
                $scope.newReviewsNumber = response.data.newReviewRecordsNumber;
                $scope.newAdvantagesNumber = response.data.newAdvantagesRecordsNumber;
                $scope.newDisadvantagesNumber = response.data.newDisadvantagesRecordsNumber;
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                alert("Coś poszło nie tak");
            });
        document.getElementById('productCode').disabled = false;
    }
    
    $scope.etl = function() {
        $('#transform').attr('disabled', true);
        $('#load').attr('disabled', true);
        $('#etlDiv').hide();
        $('#extractDiv').hide();
        $('#transformDiv').hide();
        $('#loadDiv').hide();

        var productCode = $scope.productCode;
        if (productCode == null || productCode == "") {
            $('#noCodeModal').modal('show');
        } else {
            $('#waitingModal').modal('show');
            $http.get(url + '/etl/' + productCode)
            .then(function successCallback(response) { 
                $('#waitingModal').modal('hide');
                $('#etlDiv').show();
                $scope.product = response.data.product;
                $scope.pagesNumber = response.data.reviewsPagesNumber;
                $scope.reviewsNumber = response.data.reviewsNumber;
                $scope.productRecords = response.data.productRecords;
                $scope.reviewRecords = response.data.reviewRecords;
                $scope.newProductsNumber = response.data.newProductRecordsNumber;
                $scope.newReviewsNumber = response.data.newReviewRecordsNumber;
                $scope.newAdvantagesNumber = response.data.newAdvantagesRecordsNumber;
                $scope.newDisadvantagesNumber = response.data.newDisadvantagesRecordsNumber;
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                $('#wrongCodeModal').modal('show');
            });
        }
    }

    $scope.getProducts = function() {
        $http.get(url + '/products')
            .then(function successCallback(response) {
                $scope.products = response.data;
                if($scope.products !== ""){
                    var down = document.getElementById('downAll');
                    if(down != null){
                        down.style.display = "block";
                    }
                }
            }, function errorCallback(response) {
                alert("Coś poszło nie tak");
            });
    }
    
    $scope.getReviews = function() {
        var productCode = $('#productChoice').find(":selected").val();
        $http.get(url + '/reviews/' + productCode)
            .then(function successCallback(response) {
                if (response.data.length == 0) {
                    $('#noReviewsModal').modal('show');
                } else {
                    $scope.productReviews = response.data;
                }
            }, function errorCallback(response) {
                alert("Coś poszło nie tak");
            });
    }

    $scope.clear = function() {    
        var option = $("input[type='radio'][name='clearRadios']:checked").val();  
        if(option == "all"){
            $scope.records = "rekordy";
        } else if(option == "reviewsOnly") {
            $scope.records = "opinie";
        }
        $('#confirmationModal').modal('show');
    }
    
    $scope.clearDB = function() {
        $('#confirmationModal').modal('hide');
        var option = $("input[type='radio'][name='clearRadios']:checked").val();
        if (option == "all") {
            $http.get(url + '/delete/all')
                .then(function successCallback(response) {
                    var info = "Liczba usuniętych produktów: " + response.data.deletedProductsNumber + '<br>' + "Liczba usuniętych opinii: " + response.data.deletedReviewsNumber;
                    console.log(info);
                    $('#deleteModalBody').html(info);
                    $('#deleteModal').modal('show');
                }, function errorCallback(response) {
                    alert("Coś poszło nie tak");
                });
        }
        if (option == "reviewsOnly") {
            $http.get(url + '/delete/reviews')
                .then(function successCallback(response) {
                    $('#deleteModalBody').html("Liczba usuniętych opinii: " + response.data.deletedReviewsNumber);
                    $('#deleteModal').modal('show');
                }, function errorCallback(response) {
                    alert("Coś poszło nie tak");
                });
        }
    }
    
    $scope.downloadAllCSV = function() {
        $('#waitingModal').modal('show');
        $http.get(url + '/generateCSV')
            .then(function successCallback(response) {
                var cont = response.data.fileContent;
                var name = response.data.fileName;
                download(name, "csv", cont);
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                alert("Coś poszło nie tak");
            });
    }

    $scope.downloadCSV = function(code) {
        $('#waitingModal').modal('show');
        $http.get(url + '/generateCSV/' + code)
            .then(function successCallback(response) {
                var cont = response.data.fileContent;
                var name = response.data.fileName;
                download(name, "csv", cont);
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                alert("Coś poszło nie tak");
            });        
    }
    
    $scope.downloadTXT = function(code) {
        $('#waitingModal').modal('show');
        $http.get(url + '/generateTXT/' + code)
            .then(function successCallback(response) {
                var cont = response.data.fileContent;
                var name = response.data.fileName;
                download(name, "plain", cont);
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                alert("Coś poszło nie tak");
            });
    }

    $scope.downloadAllTXT = function() {
        $('#waitingModal').modal('show');
        $http.get(url + '/generateTXT')
            .then(function successCallback(response) {
                var cont = response.data.fileContent;
                var name = response.data.fileName;
                download(name, "plain", cont);
                $('#waitingModal').modal('hide');
            }, function errorCallback(response) {
                $('#waitingModal').modal('hide');
                alert("Coś poszło nie tak");
            });
        
    }

    function download(filename, type, text) {
        var downloader = document.createElement('a');
        /* WARTO ZAZNACZYC ZE PLIKI GENEROWANE SA Z KODOWANIEM utf-8  */
        downloader.setAttribute('href', 'data:text/' + type + ';charset=utf-8,' + encodeURIComponent(text));   
        downloader.setAttribute('download', filename);

        downloader.style.display = 'none';
        document.body.appendChild(downloader);

        downloader.click();

        document.body.removeChild(downloader);
    }

});