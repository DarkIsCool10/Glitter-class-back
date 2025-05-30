create or replace PROCEDURE actualizar_datos_examen (
    p_idExamen           IN NUMBER,
    p_totalPreguntas     IN NUMBER,
    p_preguntasMostrar   IN NUMBER,
    p_resultado          OUT NUMBER
) AS
    v_existe NUMBER := 0;
BEGIN
    -- Verificar si el examen existe
    SELECT COUNT(*) INTO v_existe
    FROM Examen
    WHERE idExamen = p_idExamen;

    IF v_existe = 0 THEN
        p_resultado := 0;

        RETURN;
    END IF;

    -- Actualizar los campos
    UPDATE Examen
    SET 
        cantidadPreguntas = p_totalPreguntas,
        preguntasMostradas = p_preguntasMostrar
    WHERE idExamen = p_idExamen;

    p_resultado := 1;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        p_resultado := -1;
        ROLLBACK;
END actualizar_datos_examen;

create or replace PROCEDURE agregar_pregunta_a_examen (
    p_idExamen     IN NUMBER,
    p_idPregunta   IN NUMBER,
    p_resultado    OUT NUMBER
) IS
    v_tema_examen     NUMBER;
    v_unidad_examen   NUMBER;

    v_tema_pregunta   NUMBER;
    v_unidad_pregunta NUMBER;       

    v_peso_dificultad NUMBER(5,2);
BEGIN
    -- Obtener tema y unidad del examen
    SELECT idTema, idUnidad
    INTO v_tema_examen, v_unidad_examen
    FROM Examen
    WHERE idExamen = p_idExamen;

    -- Obtener tema y unidad de la pregunta
    SELECT idTema, idUnidad
    INTO v_tema_pregunta, v_unidad_pregunta
    FROM Pregunta
    WHERE idPregunta = p_idPregunta;

    -- Validar coincidencia de tema y unidad
    IF v_tema_examen != v_tema_pregunta OR v_unidad_examen != v_unidad_pregunta THEN
        p_resultado := -1; -- Tema o unidad no coinciden
        RETURN;
    END IF;

    -- Validar si ya fue agregada
    DECLARE
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_exists
        FROM ExamenPregunta
        WHERE idExamen = p_idExamen AND idPregunta = p_idPregunta;

        IF v_exists > 0 THEN
            p_resultado := -2; -- Ya existe
            RETURN;
        END IF;
    END;

    -- Obtener peso (porcentaje) de la pregunta según dificultad
    SELECT d.peso
    INTO v_peso_dificultad
    FROM Pregunta p
    JOIN DificultadPregunta d ON p.idDificultad = d.idDificultad
    WHERE p.idPregunta = p_idPregunta;

    -- Insertar en ExamenPregunta
    INSERT INTO ExamenPregunta (idExamen, idPregunta, porcentajeEnExamen)
    VALUES (p_idExamen, p_idPregunta, v_peso_dificultad);

    -- Actualizar cantidad de preguntas en el examen
    UPDATE Examen
    SET cantidadPreguntas = (
        SELECT COUNT(*) FROM ExamenPregunta WHERE idExamen = p_idExamen
    )
    WHERE idExamen = p_idExamen;

    p_resultado := 1; -- Éxito

EXCEPTION
    WHEN OTHERS THEN
        p_resultado := -99;
END;

create or replace PROCEDURE crear_examen (
    p_idGrupo             IN NUMBER,
    p_idDocente           IN NUMBER,
    p_idTema              IN NUMBER,
    p_titulo              IN VARCHAR2,
    p_descripcion         IN CLOB,
    p_cantidadPreguntas   IN NUMBER,
    p_preguntasMostradas  IN NUMBER,
    p_tiempoLimite        IN NUMBER,
    p_fechaDisponible     IN TIMESTAMP,
    p_fechaCierre         IN TIMESTAMP,
    p_pesoEnCurso         IN NUMBER,
    p_umbralAprobacion    IN NUMBER,
    p_idUnidad            IN NUMBER,
    p_idExamen            OUT NUMBER,
    p_idTemas             OUT NUMBER,
    p_resultado           OUT NUMBER
) AS
    v_fecha_actual TIMESTAMP := SYSTIMESTAMP;
BEGIN
    -- 1. Validación de fechas
    IF p_fechaDisponible < v_fecha_actual THEN
        p_resultado := 0;
        RETURN;
    END IF;
    
    IF p_fechaCierre < v_fecha_actual THEN
        p_resultado := 0;
        RETURN;
    END IF;
    
    IF p_fechaCierre <= p_fechaDisponible THEN
        p_resultado := 0;
        RETURN;
    END IF;

    -- 2. Insertar en la tabla Examen si pasó todas las validaciones
    INSERT INTO Examen (
        idGrupo,
        idDocente,
        idTema,
        titulo,
        descripcion,
        cantidadPreguntas,
        preguntasMostradas,
        tiempoLimite,
        fechaDisponible,
        fechaCierre,
        pesoEnCurso,
        umbralAprobacion,
        idUnidad,
        idEstado
    )
    VALUES (
        p_idGrupo,
        p_idDocente,
        p_idTema,
        p_titulo,
        p_descripcion,
        p_cantidadPreguntas,
        p_preguntasMostradas,
        p_tiempoLimite,
        p_fechaDisponible,
        p_fechaCierre,
        p_pesoEnCurso,
        p_umbralAprobacion,
        p_idUnidad,
        1
    )
    RETURNING idExamen, idTema INTO p_idExamen, p_idTemas;

    p_resultado := 1;
EXCEPTION
    WHEN OTHERS THEN
        p_resultado := 0;
        ROLLBACK;
END crear_examen;

create or replace PROCEDURE crear_opcion_respuesta (
    p_idPregunta        IN NUMBER,
    p_textoOpcion       IN CLOB,
    p_textoPareja       IN CLOB,
    p_idTipoRespuesta   IN NUMBER,
    p_idOpcion          OUT NUMBER,
    p_resultado         OUT NUMBER
) IS
    v_tipoPregunta        NUMBER;
    v_opcionesCorrectas   NUMBER := 0;
    v_orden               NUMBER;
BEGIN
    -- Validar existencia de la pregunta y obtener su tipo
    BEGIN
        SELECT idTipo INTO v_tipoPregunta
        FROM Pregunta
        WHERE idPregunta = p_idPregunta;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            p_resultado := -1; -- Pregunta no encontrada
            RETURN;
    END;

    -- Validaciones según tipo de pregunta
    IF v_tipoPregunta IN (1, 6) THEN
        -- Solo una opción correcta permitida
        IF p_idTipoRespuesta = 1 THEN
            SELECT COUNT(*) INTO v_opcionesCorrectas
            FROM OpcionRespuesta
            WHERE idPregunta = p_idPregunta AND idTipoRespuesta = 1;

            IF v_opcionesCorrectas >= 1 THEN
                p_resultado := -2; -- Ya existe una correcta
                RETURN;
            END IF;
        END IF;

    ELSIF v_tipoPregunta = 4 THEN
        -- Emparejar: texto y pareja son obligatorios
        IF p_textoOpcion IS NULL OR p_textoPareja IS NULL THEN
            p_resultado := -3; -- Falta alguno de los textos de emparejamiento
            RETURN;
        END IF;

    ELSIF v_tipoPregunta = 3 THEN
        -- Falso/Verdadero: solo se permiten 2 opciones
        SELECT COUNT(*) INTO v_opcionesCorrectas
        FROM OpcionRespuesta
        WHERE idPregunta = p_idPregunta;

        IF v_opcionesCorrectas >= 2 THEN
            p_resultado := -4; -- Ya existen 2 opciones
            RETURN;
            IF v_opcionesCorrectas >= 1 THEN
                p_resultado := -2; -- Ya existe una correcta
                RETURN;
            END IF;
        END IF;
    END IF;

    -- Insertar la opción
    INSERT INTO OpcionRespuesta (
        idPregunta, textoOpcion, textoPareja, idTipoRespuesta
    ) VALUES (
        p_idPregunta, p_textoOpcion, p_textoPareja, p_idTipoRespuesta
    )
    RETURNING idOpcion INTO p_idOpcion;

    p_resultado := 1; -- Éxito

EXCEPTION
    WHEN OTHERS THEN
        p_resultado := -99;
        p_idOpcion := NULL;
END;

create or replace PROCEDURE crear_pregunta (
    p_enunciado        IN CLOB,
    p_idTema           IN NUMBER,
    p_idDificultad     IN NUMBER,
    p_idTipo           IN NUMBER,
    p_porcentajeNota   IN NUMBER,
    p_idVisibilidad    IN NUMBER,
    p_idDocente        IN NUMBER,
    p_idUnidad         IN NUMBER,
    p_idEstado         IN NUMBER, 
    p_idPregunta       OUT NUMBER,
    p_resultado        OUT NUMBER
) IS
BEGIN
    INSERT INTO Pregunta (
        enunciado, idTema, idDificultad, idTipo,
         porcentajeNota, idVisibilidad,
        idDocente, idUnidad, idEstado, fechaCreacion
    )
    VALUES (
        p_enunciado, p_idTema, p_idDificultad, p_idTipo,
        p_porcentajeNota, p_idVisibilidad,
        p_idDocente, p_idUnidad, p_idEstado, SYSTIMESTAMP
    )
    RETURNING idPregunta INTO p_idPregunta;

    p_resultado := 1;
EXCEPTION
    WHEN OTHERS THEN
        p_idPregunta := NULL;
        p_resultado := 0;
END;

create or replace PROCEDURE finalizar_intento (
    p_idIntento      IN NUMBER,
    p_resultado      OUT NUMBER,
    p_calificacion   OUT NUMBER
) IS
    v_total   NUMBER := 0;
    v_examen  NUMBER;
    v_inicio  TIMESTAMP;
BEGIN
    -- Calcular nota total
    SELECT SUM(puntajeObtenido), idExamen, fechaInicio
    INTO v_total, v_examen, v_inicio
    FROM IntentoExamen i
    JOIN RespuestaEstudiante r ON i.idIntento = r.idIntento
    WHERE i.idIntento = p_idIntento
    GROUP BY i.idIntento, idExamen, fechaInicio;

    -- Actualizar intento
    UPDATE IntentoExamen
    SET calificacion = v_total,
        fechaFin = SYSTIMESTAMP,
        tiempoEmpleado = ROUND(
            (CAST(SYSTIMESTAMP AS DATE) - CAST(v_inicio AS DATE)) * 24 * 60
        ),
        idestado = 5
    WHERE idIntento = p_idIntento;

    p_calificacion := v_total;
    p_resultado := 1;

EXCEPTION
    WHEN OTHERS THEN
        p_resultado := -1;
        p_calificacion := NULL;
END;

create or replace PROCEDURE generar_examen_estudiante (
    p_idExamen       IN NUMBER,
    p_idEstudiante   IN NUMBER,
    p_idIntento      OUT NUMBER,
    p_resultado      OUT NUMBER
) IS
    v_preguntasMostradas   NUMBER;
    v_totalPesoDificultad  NUMBER;
    v_existente            NUMBER;
BEGIN
    -- Verificar si el estudiante ya tiene un intento para ese examen
    SELECT COUNT(*) INTO v_existente
    FROM IntentoExamen
    WHERE idExamen = p_idExamen AND idEstudiante = p_idEstudiante;

    IF v_existente > 0 THEN
        p_resultado := -1; -- Ya existe intento
        p_idIntento := NULL;
        RETURN;
    END IF;

    -- Obtener cantidad de preguntas que se deben mostrar
    SELECT preguntasMostradas INTO v_preguntasMostradas
    FROM Examen
    WHERE idExamen = p_idExamen;

    -- Crear intento para el estudiante
    INSERT INTO IntentoExamen (idEstudiante, idExamen, fechaInicio, ipacceso)
    VALUES (p_idEstudiante, p_idExamen, SYSTIMESTAMP, '192.168.10.4')
    RETURNING idIntento INTO p_idIntento;

    -- Seleccionar preguntas aleatorias
    DECLARE
        CURSOR preguntas_cur IS
            SELECT p.idPregunta, d.peso
            FROM ExamenPregunta ep
            JOIN Pregunta p ON ep.idPregunta = p.idPregunta
            JOIN DificultadPregunta d ON p.idDificultad = d.idDificultad
            WHERE ep.idExamen = p_idExamen
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST v_preguntasMostradas ROWS ONLY;

        v_sumPeso NUMBER := 0;
        TYPE t_preg IS TABLE OF Pregunta.idPregunta%TYPE INDEX BY PLS_INTEGER;
        TYPE t_peso IS TABLE OF NUMBER INDEX BY PLS_INTEGER;
        preguntas t_preg;
        pesos     t_peso;
        idx       INTEGER := 1;
    BEGIN
        -- Recolectar preguntas
        FOR pregunta_rec IN preguntas_cur LOOP
            preguntas(idx) := pregunta_rec.idPregunta;
            pesos(idx) := pregunta_rec.peso;
            v_sumPeso := v_sumPeso + pregunta_rec.peso;
            idx := idx + 1;
        END LOOP;

        -- Insertar preguntas del examen estudiante
        FOR i IN 1 .. preguntas.COUNT LOOP
            INSERT INTO ExamenEstudiante (
                idIntento, idExamen, idPregunta, idEstudiante, porcentajePregunta
            ) VALUES (
                p_idIntento,
                p_idExamen,
                preguntas(i),
                p_idEstudiante,
                ROUND((pesos(i) / v_sumPeso) * 100, 2)
            );
        END LOOP;
    END;

    p_resultado := 1; -- Exito
EXCEPTION
    WHEN OTHERS THEN
        p_resultado := -99;
        p_idIntento := NULL;
END;

create or replace PROCEDURE obtener_usuario_detalle (
    p_idUsuario       IN  NUMBER,
    p_nombre          OUT VARCHAR2,
    p_apellido        OUT VARCHAR2,
    p_correo          OUT VARCHAR2,
    p_idUnidad        OUT NUMBER,
    p_nombreUnidad    OUT VARCHAR2,
    p_fechaRegistro   OUT TIMESTAMP,
    p_estado          OUT VARCHAR2,
    p_idRol           OUT NUMBER,
    p_resultado       OUT NUMBER
) IS
BEGIN
    SELECT u.nombre, u.apellido, c.correo, ua.idUnidad, ua.nombre,
           u.fechaRegistro, e.nombre, c.idRol
    INTO   p_nombre, p_apellido, p_correo, p_idUnidad, p_nombreUnidad,
           p_fechaRegistro, p_estado, p_idRol
    FROM   Usuario u
    JOIN   EstadoGeneral e ON u.idEstado = e.idEstado
    JOIN   unidadacademica ua ON u.idUnidad = ua.idunidad
    JOIN   CredencialesAcceso c ON u.idUsuario = c.idUsuario
    WHERE  u.idUsuario = p_idUsuario;

    p_resultado := 1;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_resultado := 0;
    WHEN OTHERS THEN
        p_resultado := -1;
END;

create or replace PROCEDURE registrar_respuesta_estudiante (
    p_idIntento        IN NUMBER,
    p_idPregunta       IN NUMBER,
    p_idOpcion         IN NUMBER,
    p_resultado        OUT NUMBER
) IS
    v_idExamen         NUMBER;
    v_idEstudiante     NUMBER;
    v_puntaje          NUMBER := 0;
    v_tipoRespuesta    NUMBER;
BEGIN
    -- Obtener el tipo de respuesta
    SELECT idTipoRespuesta INTO v_tipoRespuesta
    FROM OpcionRespuesta
    WHERE idPregunta = p_idPregunta AND idOpcion = p_idOpcion;

    -- Obtener idExamen e idEstudiante
    SELECT idExamen, idEstudiante INTO v_idExamen, v_idEstudiante
    FROM IntentoExamen
    WHERE idIntento = p_idIntento;

    -- Si la opción es correcta, obtener puntaje
    IF v_tipoRespuesta = 1 THEN
        SELECT porcentajePregunta INTO v_puntaje
        FROM ExamenEstudiante
        WHERE idIntento = p_idIntento AND idPregunta = p_idPregunta;
    END IF;

    -- Insertar respuesta
    INSERT INTO RespuestaEstudiante (
        idIntento, idPregunta, idOpcion, puntajeObtenido
    ) VALUES (
        p_idIntento, p_idPregunta, p_idOpcion, v_puntaje
    );

    p_resultado := 1;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_resultado := -1;
    WHEN OTHERS THEN
        p_resultado := -2;
END;

create or replace PROCEDURE validar_login(
    p_correo      IN  VARCHAR2,
    p_contrasena  IN  VARCHAR2,
    p_idusuario   OUT NUMBER,
    p_idrol       OUT NUMBER,
    p_result      OUT NUMBER
) AS
BEGIN
    SELECT idusuario, idrol
    INTO p_idusuario, p_idrol
    FROM credencialesacceso
    WHERE UPPER(TRIM(correo)) = UPPER(TRIM(p_correo))
      AND UPPER(TRIM(contrasena)) = UPPER(TRIM(p_contrasena));

    p_result := 1;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_result := 0;
        p_idusuario := NULL;
        p_idrol := NULL;
END;

-- ============================

-- DISPARADORES

-- ============================

create or replace TRIGGER trg_no_editar_examen_presentado
BEFORE UPDATE ON Examen
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM IntentoExamen
    WHERE idExamen = :OLD.idExamen;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'No se puede modificar un examen que ya fue presentado por al menos un estudiante.');
    END IF;
END;

create or replace TRIGGER trg_validar_preguntas_en_examen
BEFORE INSERT ON ExamenPregunta
FOR EACH ROW
DECLARE
    v_idTemaExamen Examen.idTema%TYPE;
    v_idUnidadExamen Examen.idUnidad%TYPE;
    v_idTemaPregunta Pregunta.idTema%TYPE;
    v_idUnidadPregunta Pregunta.idUnidad%TYPE;
BEGIN
    -- Obtener tema y unidad del examen
    SELECT idTema, idUnidad
    INTO v_idTemaExamen, v_idUnidadExamen
    FROM Examen
    WHERE idExamen = :NEW.idExamen;

    -- Obtener tema y unidad de la pregunta
    SELECT idTema, idUnidad
    INTO v_idTemaPregunta, v_idUnidadPregunta
    FROM Pregunta
    WHERE idPregunta = :NEW.idPregunta;

    -- Validar que coincidan
    IF v_idTemaExamen != v_idTemaPregunta OR v_idUnidadExamen != v_idUnidadPregunta THEN
        RAISE_APPLICATION_ERROR(-20002, 'La pregunta no pertenece al mismo tema o unidad del examen.');
    END IF;
END;

create or replace TRIGGER trg_validar_tiempo_respuesta
BEFORE INSERT ON RespuestaEstudiante
FOR EACH ROW
DECLARE
    v_fechaFin   TIMESTAMP;
    v_tiempoLimite NUMBER;
    v_fechaMaxima TIMESTAMP;
BEGIN
    -- Obtener la fecha de inicio del intento y el tiempo límite del examen
    SELECT ie.fechaInicio + NUMTODSINTERVAL(e.tiempoLimite, 'MINUTE')
    INTO v_fechaMaxima
    FROM IntentoExamen ie
    JOIN Examen e ON ie.idExamen = e.idExamen
    WHERE ie.idIntento = :NEW.idIntento;

    IF SYSTIMESTAMP > v_fechaMaxima THEN
        RAISE_APPLICATION_ERROR(-20002, 'Tiempo excedido: no se puede guardar una respuesta fuera del límite del examen.');
    END IF;
END;
