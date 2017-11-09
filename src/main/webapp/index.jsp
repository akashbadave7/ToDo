<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Calorie Tracker</title>
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/pure/pure.css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/pure/grids-responsive.css">
    <link rel="stylesheet" type="text/css" href="css/pure-theme.css">
    <link rel="stylesheet" type="text/css" href="css/calories-tracker.css">
</head>
<body class="pure-skin-theme" id="loginApp">

    <header class="page-header pure-g">
        <div class="pure-u-lg-1-2 pure-u-1">
            <a class="pure-menu-heading" href="#">
                <img class="logo" src="/resources/img/logo.png">
            </a>
            <span class="header-element page-title">Calorie Tracker</span>
        </div>
    </header>

    <main ng-controller="BaseFormCtrl">

        <tt-error-messages extra-styles="access-screen"></tt-error-messages>

        <div class="login-container">
            <form ng-submit="onLogin()" name="form" class="pure-form login-form" novalidate ng-controller="LoginCtrl">
                <fieldset class="pure-group">
                    <legend>Log In</legend>
                    <div class="form-field">
                        <input type="text" ng-focus="focus('username')"  ng-blur="blur('username')" ng-model="vm.username"
                               name="username" class="pure-input-1" placeholder="Username" required ng-minlength="6">

                        <div class="error-messages" ng-show="isMessagesVisible('username')" ng-messages="form.username.$error" ng-cloak>
                            <div ng-message="required">The username is mandatory</div>
                            <div ng-message="minlength">must have minimum 6 characters</div>
                        </div>
                    </div>
                    <div class="last-form-field">
                        <input type="password" ng-focus="focus('password')"  ng-blur="blur('password')" ng-model="vm.password"
                               name="password" class="pure-input-1" placeholder="Password" required ng-minlength="6"
                               pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}">
                        <div class="error-messages" ng-show="isMessagesVisible('password')" ng-messages="form.password.$error" ng-cloak>
                            <div ng-message="required">The password is mandatory</div>
                            <div ng-message="minlength">must have minimum 6 characters</div>
                            <div ng-message="pattern">At least one number and uppercase</div>
                        </div>
                    </div>
                </fieldset>

                <button type="submit" class="pure-button pure-input-1 pure-button-primary">Log In</button>
                <a class="new-user-link" href="/resources/public/new-user.html">New user?</a>

            </form>
        </div>

    </main>

    <script type="text/javascript" data-main="/resources/public/js/run-loggin-app"
            src="/resources/bower_components/requirejs/require.js"></script>


</body>
</html>