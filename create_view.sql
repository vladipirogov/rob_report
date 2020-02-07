CREATE VIEW dbo.view_report AS
with el as (select 1 as el_num, rep.route_run_id, rep.el_1_id as el_id, rep.el_1_hrs as hrs, el_1_prf as prf, el_1_al_id as al_id, el_1_pal as pal, el_1_t_max as t_max, el_1_t_belt_max as t_belt_max from report rep
			union
			select 2 as el_num, rep.route_run_id, rep.el_2_id as el_id, rep.el_2_hrs as hrs, el_2_prf as prf, el_2_al_id as al_id, el_2_pal as pal, el_2_t_max as t_max, el_2_t_belt_max as t_belt_max from report rep
			union
			select 3 as el_num, rep.route_run_id, rep.el_3_id as el_id, rep.el_3_hrs as hrs, el_3_prf as prf, el_3_al_id as al_id, el_3_pal as pal, el_3_t_max as t_max, el_3_t_belt_max as t_belt_max from report rep
			union
			select 4 as el_num, rep.route_run_id, rep.el_4_id as el_id, rep.el_4_hrs as hrs, el_4_prf as prf, el_4_al_id as al_id, el_4_pal as pal, el_4_t_max as t_max, el_4_t_belt_max as t_belt_max from report rep
			union
			select 5 as el_num, rep.route_run_id, rep.el_5_id as el_id, rep.el_5_hrs as hrs, el_5_prf as prf, el_5_al_id as al_id, el_5_pal as pal, el_5_t_max as t_max, el_5_t_belt_max as t_belt_max from report rep
			union
			select 6 as el_num, rep.route_run_id, rep.el_6_id as el_id, rep.el_6_hrs as hrs, el_6_prf as prf, el_6_al_id as al_id, el_6_pal as pal, el_6_t_max as t_max, el_6_t_belt_max as t_belt_max from report rep
			union
			select 7 as el_num, rep.route_run_id, rep.el_7_id as el_id, rep.el_7_hrs as hrs, el_7_prf as prf, el_7_al_id as al_id, el_7_pal as pal, el_7_t_max as t_max, el_7_t_belt_max as t_belt_max from report rep
			union
			select 8 as el_num, rep.route_run_id, rep.el_8_id as el_id, rep.el_8_hrs as hrs, el_8_prf as prf, el_8_al_id as al_id, el_8_pal as pal, el_8_t_max as t_max, el_8_t_belt_max as t_belt_max from report rep
			union
			select 9 as el_num, rep.route_run_id, rep.el_9_id as el_id, rep.el_9_hrs as hrs, el_9_prf as prf, el_9_al_id as al_id, el_9_pal as pal, el_9_t_max as t_max, el_9_t_belt_max as t_belt_max from report rep
			union
			select 10 as el_num, rep.route_run_id, rep.el_10_id as el_id, rep.el_10_hrs as hrs, el_10_prf as prf, el_10_al_id as al_id, el_10_pal as pal, el_10_t_max as t_max, el_10_t_belt_max as t_belt_max from report rep
			union
			select 11 as el_num, rep.route_run_id, rep.el_11_id as el_id, rep.el_11_hrs as hrs, el_11_prf as prf, el_11_al_id as al_id, el_11_pal as pal, el_11_t_max as t_max, el_11_t_belt_max as t_belt_max from report rep
			union
			select 12 as el_num, rep.route_run_id, rep.el_12_id as el_id, rep.el_12_hrs as hrs, el_12_prf as prf, el_12_al_id as al_id, el_12_pal as pal, el_12_t_max as t_max, el_12_t_belt_max as t_belt_max from report rep
			)
select 
	rep.route_run_id,
	rep.route_id, 
	rep.date_time_start,
	rep.date_time_end,
	rep.time_work, 
	el.el_num,
	name.id_name as name,
	el.hrs,
	(select  AVG(dval.power) from dval where el.el_id = dval.el_id and el.route_run_id = dval.route_run_id)as avg_load,
	el.prf,
	alarm.id_alarm,
	el.pal,
	el.t_max,
	el.t_belt_max
from 
	dbo.report rep
left join el
on el.route_run_id = rep.route_run_id
left join id_name as name
on el.el_id = name.id_
left join id_alarm as alarm
on alarm.id_ = el.al_id
where 
name.id_name is not null


