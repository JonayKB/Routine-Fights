Entidades (Nodos)
1. User
Propiedades:
id: Integer (PK)
username: String
email: String
password: String
nacionality: String
phoneNumber: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

3. Post
Propiedades:
id: Integer (PK)
image: String
streak: Integer
points: Integer
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

4. Comment
Propiedades:
id: Integer (PK)
message: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

6. Report
Propiedades:
id: Integer (PK)
message: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

8. Badge
Propiedades:
id: Integer (PK)
icon: String
level: Integer
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

10. CommunityEvent
Propiedades:
id: Integer (PK)
name: String
totalRequiered: Integer
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

12. Activity
Propiedades:
id: Integer (PK)
icon: String
name: String
description: String
timeRate: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

14. Category
Propiedades:
id: Integer (PK)
name: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

16. Team
Propiedades:
id: Integer (PK)
name: String
createdAt: Timestamp
updatedAt: Timestamp
deletedAt: Timestamp

18. Meeting
Propiedades:
id: Integer (PK)
date: Timestamp
latitude: Float (10,6)
longitude: Float (10,6)

Relaciones
FOLLOWS

Entre: (User) → (User)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Representa que un usuario sigue a otro.
POSTED

Entre: (User) → (Post)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Indica que un usuario creó un post.
RELATED_TO

Entre: (Post) → (Activity)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Asocia un post a una actividad determinada.
COMMENTED

Entre: (User) → (Comment)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Relaciona al usuario que realiza un comentario con el comentario creado.
ON

Entre: (Comment) → (Post)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Indica que un comentario pertenece a un post.
REPLIED_TO

Entre: (Comment) → (Comment)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Permite modelar respuestas, conectando un comentario con otro al que responde.
REPORTED

Entre: (User) → (Report)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Un usuario genera un reporte.
Complementaria:
(Report) → (Post) para indicar sobre qué post se ha realizado el reporte.
HAS_BADGE

Entre: (User) → (Badge)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Indica que un usuario posee un determinado badge.
ASSOCIATED_WITH

Entre: (Badge) → (CommunityEvent)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Relaciona un badge con un evento comunitario.
CREATED_ACTIVITY

Entre: (User) → (Activity)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Indica el usuario que crea la actividad.
BELONGS_TO

Entre: (Activity) → (Category)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Establece a qué categoría pertenece una actividad.
MEMBER_OF

Entre: (User) → (Team)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Representa la membresía de un usuario en un equipo.
HAS_MEETING

Entre: (Team) → (Meeting)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Un equipo tiene asignada una reunión.
ATTENDED

Entre: (User) → (Meeting)
Propiedades: createdAt, updatedAt, deletedAt
Descripción: Registra la asistencia de un usuario a una reunión.
PART_OF

Entre: (Meeting) → (CommunityEvent)
Propiedades (Opcionales): createdAt, updatedAt, deletedAt
Descripción: Indica que una reunión forma parte de un evento comunitario.
