angular.module('tb-color-picker', [])
    .run(['$templateCache', function($templateCache) {
        $templateCache.put('color-picker.tpl.html', '<div class="color-picker">'+
            '<img class="selected-color" src="images/color.svg" style="border:none;">' +
            '<div class="color-palette">'+
                '<div ng-repeat="option in vm.options"'+
                'ng-style="{\'background-color\': option.color}"'+
                'ng-class="{\'palette-selected-color\': option.color == note.color, \'transparent-color\': option.color == \'transparent\'}"'+
                'ng-click="vm.changeColor(option)">'+'<md-tooltip class="tool" md-direction="bottom">{{option.name}}</md-tooltip>'+'</div>'+
                
            '</div>'+
        '</div>');
    }])
    .directive('colorPicker', function () {
        return {
            restrict: 'E',
            templateUrl: 'color-picker.tpl.html',
            replace: true,
            controller: colorPickerDirectiveController,
            controllerAs: 'vm',
            bindToController: {
                color: '=',
                options: '=',
                onColorChanged: '&'
            }
        };

        function colorPickerDirectiveController() {
            var vm = this;
            
            vm.changeColor = function (option) {
                if(vm.color != option.color) {
                    var old = vm.color;
                    vm.color = option.color;
                    vm.onColorChanged({newColor: option.color, oldColor: old});
                }
            }

        }
    });