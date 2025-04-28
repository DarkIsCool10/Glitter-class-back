CREATE OR REPLACE PROCEDURE obtener_usuarios(
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
OPEN p_cursor FOR
SELECT
    IDUSUARIO,
    NOMBRE,
    APELLIDO,
    IDUNIDAD,
    FECHAREGISTRO,
    ACTIVO
FROM USUARIO
ORDER BY APELLIDO, NOMBRE;
END obtener_usuarios;
/
--Ejemplo de un procedimiento almacenado para obtener todos los usuarios de la base de datos