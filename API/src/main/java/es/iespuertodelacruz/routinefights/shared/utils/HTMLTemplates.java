package es.iespuertodelacruz.routinefights.shared.utils;

public class HTMLTemplates {
    private HTMLTemplates() {
    }

    public static final String VERIFICATION_EMAIL = """
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f4f4f9;
                        color: #333;
                    }
                    .container {
                        max-width: 600px;
                        margin: 50px auto;
                        padding: 20px;
                        background: #ffffff;
                        border-radius: 10px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background:rgb(130, 76, 175);
                        color: #ffffff;
                        padding: 10px;
                        border-radius: 10px 10px 0 0;
                        text-align: center;
                    }
                    .content {
                        padding: 20px;
                        text-align: center;
                    }
                    .button {
                        display: inline-block;
                        margin-top: 20px;
                        padding: 10px 20px;
                        font-size: 16px;
                        color: #ffffff;
                        background-color:rgb(175, 76, 167);
                        text-decoration: none;
                        border-radius: 5px;
                    }
                    .button:hover {
                        background-color:rgb(139, 69, 160);
                    }
                    .footer {
                        margin-top: 20px;
                        font-size: 12px;
                        color: #999;
                        text-align: center;
                    }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h1>Verificación de Cuenta</h1>
                    </div>
                    <div class='content'>
                        <p>Gracias por registrarte. Para activar tu cuenta, haz clic en el botón de abajo:</p>
                        <a href='http://167.172.188.177:8080/auth/verify/%s/%s' class='button'>Verificar Cuenta</a>
                    </div>
                    <div class='footer'>
                        <p>Si no realizaste esta solicitud, puedes ignorar este mensaje.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

    public static final String NEED_EMAIL_TOKEN = """
            <html>
            <head><title>Error</title></head>
            <body>
                <h1>Faltan datos</h1>
                <p>Necesitas proporcionar correo y token para verificar tu cuenta.</p>
            </body>
            </html>
            """;
    public static final String ERROR = """
            <html>
            <head>
                <title>Error</title>
            </head>
            <body>
                <h1>Error al verificar</h1>
                <p>Ha ocurrido un error al verificar tu cuenta.</p>
                <p>%s</p>
            </body>
            </html>
            """;
    public static final String VERIFIED = """
            <html>
            <head><title>Verificado</title></head>
            <body>
                <h1>Cuenta verificada</h1>
                <p>Tu cuenta ha sido verificada correctamente.</p>
            </body>
            </html>
            """;
    public static final String BAD_REQUEST = """
            <html>
            <head><title>Error</title></head>
            <body>
                <h1>Error al verificar</h1>
                <p>Ha ocurrido un error al verificar tu cuenta.</p>
            </body>
            </html>
            """;
}
