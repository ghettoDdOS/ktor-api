CREATE TABLE "Chair" (
	"Id" serial NOT NULL,
	"IdFaculty" integer NOT NULL,
	"NameChair" varchar(255) NOT NULL,
	"ShortNameChair" varchar(50) NOT NULL,
	CONSTRAINT "Chair_pk" PRIMARY KEY ("Id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "Teacher" (
	"Id" serial NOT NULL,
	"IdChair" integer NOT NULL,
	"IdPost" integer NOT NULL,
	"FirstName" varchar(255) NOT NULL,
	"SecondName" varchar(255) NOT NULL,
	"LastName" varchar(255),
	"Phone" varchar(15) NOT NULL,
	"Email" varchar(20) NOT NULL,
	CONSTRAINT "Teacher_pk" PRIMARY KEY ("Id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "Post" (
	"Id" serial NOT NULL,
	"NamePost" varchar(255) NOT NULL,
	CONSTRAINT "Post_pk" PRIMARY KEY ("Id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "Faculty" (
	"Id" serial NOT NULL,
	"NameFaculty" varchar(255) NOT NULL,
	"ShortNameFaculty" varchar(50) NOT NULL,
	CONSTRAINT "Faculty_pk" PRIMARY KEY ("Id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "Chair" ADD CONSTRAINT "Chair_fk0" FOREIGN KEY ("IdFaculty") REFERENCES "Faculty"("Id");
ALTER TABLE "Teacher" ADD CONSTRAINT "Teacher_fk0" FOREIGN KEY ("IdChair") REFERENCES "Chair"("Id");
ALTER TABLE "Teacher" ADD CONSTRAINT "Teacher_fk1" FOREIGN KEY ("IdPost") REFERENCES "Post"("Id");

INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Менеджмента и предпринимательства','МИП');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Торгового дела','ТД');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Компьютерных технологий и информационной безопасности','КТиИБ');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Учетно-экономический','УЭФ');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Экономики и финансов','ЭИФ');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Юридический','ЮФ');
INSERT INTO "Faculty"("NameFaculty", "ShortNameFaculty") VALUES ('Лингвистики и журналистики','ЛИЖ');

INSERT INTO "Post"("NamePost") VALUES ('Доцент');
INSERT INTO "Post"("NamePost") VALUES ('Профессор');
INSERT INTO "Post"("NamePost") VALUES ('Старший преподаватель');
INSERT INTO "Post"("NamePost") VALUES ('Ассистент');

INSERT INTO "Chair"("IdFaculty", "Code", "NameChair", "ShortNameChair") VALUES (3,22,'Информационных систем и прикладной информатики','ИСиПИ');

INSERT INTO "Teacher"("IdChair", "IdPost", "FirstName", "SecondName", "LastName", "Phone", "Email") VALUES (1,1,'Сергей','Глушенко','Андреевич','+7 9280823234', 'mail@mail.ru');
