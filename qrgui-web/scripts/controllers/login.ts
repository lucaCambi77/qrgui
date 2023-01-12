import angular from 'angular';

/**
 * @ngdoc function
 * @name qrGuiApp.controller:LoginCtrl
 * @description # LoginCtrl Controller of the qrGuiApp
 */


angular.module('qrGuiApp').controller('LoginCtrl', ['$rootScope', 'LoginFactory', '$location',

    function LoginCtrl($rootScope, LoginFactory, $location) {

        var auth = this;
        auth.login = Login;

        function Login() {

            LoginFactory.GetUserProperties({
                username: auth.username,
                password: auth.password,
                locale: auth.locale,
                url: auth.absUrl
            });

        }

    }]);