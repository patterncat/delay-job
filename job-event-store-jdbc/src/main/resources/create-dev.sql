DROP TABLE IF EXISTS "public"."job_event_log";
CREATE TABLE "public"."job_event_log" (
	"id" serial PRIMARY KEY,
	"namespace" varchar(255) COLLATE "default",
	"queue" varchar(255) COLLATE "default",
	"worker_name" varchar(255) COLLATE "default",
	"event_timestamp" int8 NOT NULL,
	"job_class" varchar(255) COLLATE "default",
	"job_id" varchar(255) COLLATE "default",
	"job_event_type" int4,
	"job_type" int4,
	"job_args" text COLLATE "default",
	"job_vars" text COLLATE "default",
	"job_unknown_fields" text COLLATE "default",
	"result" text COLLATE "default",
	"runner" varchar(255) COLLATE "default",
	"throwable" text COLLATE "default",
	"frequency" int8 NOT NULL,
	"future" int8 NOT NULL,
	"create_time" timestamp(6) NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."job_event_log" OWNER TO "postgres";