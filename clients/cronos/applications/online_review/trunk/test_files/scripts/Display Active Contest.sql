    SELECT cqx.query_id,
     q.text,
     q.name,
     q.ranking,
     q.column_index,
     cqx.sort_order 
    FROM command c, query q, command_query_xref cqx 
    WHERE (c.command_desc = 'active_contests' or c.command_desc = 'contest_status')
    AND cqx.command_id = c.command_id 
    AND q.query_id = cqx.query_id 
    ORDER BY cqx.sort_order ASC 
    
command_id,command_desc,command_group_id
26195,active_contests,13337
26300,contest_status,13337    

query_id,text,name,ranking,column_index,sort_order
26404,select pi1.end_date initial_submission_date
     , cvd.price
     , cc.component_name
     , cc.component_id
     , cv.phase_id
     , cl.description
     , pcat.category_name catalog_name
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version
           and ci.rating > 0) as total_rated_inquiries
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version
           and ci.rating = 0) as total_unrated_inquiries
      , (SELECT count(submission_id)
            FROM submission sb
           where sb.project_id = p.project_id
             and sb.cur_version = 1
             and sb.submission_type = 1
             and sb.is_removed = 0 ) as total_submissions
      , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version) as total_inquiries
      , p.project_id
      , cv.version_text
      , (pi1.start_date + 3 UNITS DAY) as reg_end_date
      , p.max_unrated_registrants
      , (select count(*) from contest_project_xref where project_id = p.project_id) as tourny_project
      , (select category_id from comp_categories where component_id = cc.component_id and category_id = 22774808) as aol_brand
    from comp_versions cv
       , comp_catalog cc
       , categories pcat
       , project p
       , phase_instance pi1
       , comp_version_dates cvd
       , comp_level cl
   where cv.comp_vers_id = p.comp_vers_id
     and cv.phase_id-111 = p.project_type_id
     and p.cur_version = 1
     and cc.component_id = cv.component_id
     and cc.status_id = 102
     and pi1.start_date <= CURRENT
     and pi1.end_date > CURRENT
     and pcat.category_id = cc.root_category_id
     and pi1.project_id = p.project_id
     and pi1.phase_id = 1
     and pi1.cur_version = 1
     and p.PROJECT_STAT_ID in (1,3)
     and pcat.category_id in (8459260,5801776,5801777,5801778,5801779)
     and cvd.comp_vers_id = cv.comp_vers_id
     and cvd.phase_id = cv.phase_id
     and cvd.level_id = cl.level_id
     and cv.phase_id = 113
     and p.project_id not in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21657411, 21657449)
order by initial_submission_date, pcat.category_name, cc.component_name,dev_contests,0,null,null
26405,select pi1.end_date initial_submission_date
     , cvd.price
     , cc.component_name
     , cc.component_id
     , cv.phase_id
     , cl.description
     , pcat.category_name catalog_name
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.rating > 0
           and ci.version = cv.version) as total_rated_inquiries
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version
           and ci.rating = 0) as total_unrated_inquiries
      , (SELECT count(submission_id)
            FROM submission sb
           where sb.project_id = p.project_id
             and sb.cur_version = 1
             and sb.submission_type = 1
             and sb.is_removed = 0 ) as total_submissions
      , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version) as total_inquiries
      , p.project_id
      , cv.version_text
      , (pi1.start_date + 3 UNITS DAY) as reg_end_date
      , p.max_unrated_registrants
      , (select count(*) from contest_project_xref where project_id = p.project_id) as tourny_project
      , (select category_id from comp_categories where component_id = cc.component_id and category_id = 22774808) as aol_brand
    from comp_versions cv
       , comp_catalog cc
       , categories pcat
       , project p
       , phase_instance pi1
       , comp_version_dates cvd
       , comp_level cl
   where cv.comp_vers_id = p.comp_vers_id
     and cv.phase_id-111 = p.project_type_id
     and p.cur_version = 1
     and cc.component_id = cv.component_id
     and cc.status_id = 102
     and pi1.start_date <= CURRENT
     and pi1.end_date > CURRENT
     and pcat.category_id = cc.root_category_id
     and pi1.project_id = p.project_id
     and pi1.phase_id = 1
     and pi1.cur_version = 1
     and p.PROJECT_STAT_ID in (1,3)
     and pcat.category_id in (8459260,5801776,5801777,5801778,5801779)
     and cvd.comp_vers_id = cv.comp_vers_id
     and cvd.phase_id = cv.phase_id
     and cvd.level_id = cl.level_id
     and cv.phase_id = 112
     and p.project_id not in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21632776, 21663385, 21663427, 21771067, 21771121)
order by initial_submission_date, pcat.category_name, cc.component_name,design_contests,0,null,null

26503,select cc.component_name
     , cv.version_text
     , c.category_name as catalog_name
     , rp.review_phase_name as current_phase
     , pi1.end_date as reg_end_date
     , pi3.end_date as final_review_end_date
     , pr1.user_id as winner
     , pr2.user_id as second
     , p.project_type_id + 111 as phase_id
     , p.project_id
     , ph.description as phase
     , u1.handle_lower as winner_sort
     , u2.handle_lower as second_sort
     , (select count(*) from component_inquiry where project_id = p.project_id and rating = 0) as unrated_count
     , (select count(*) from component_inquiry where project_id = p.project_id and rating > 0) as rated_count
     , cc.component_id
     , cv.version
     , c.viewable
     , (select category_id from comp_categories where component_id = cc.component_id and category_id = 22774808) as aol_brand
  from project p
     , phase_instance pi1
     , phase_instance pi3
     , comp_catalog cc
     , comp_versions cv
     , categories c
     , phase_instance pi2
     , review_phase rp
     , outer (project_result pr1, user u1)
     , outer (project_result pr2, user u2)
     , phase ph
 where p.project_id = pi1.project_id
   and pi1.phase_id = 1
   and p.project_id = pi3.project_id
   and pi3.phase_id = 7
   and pi3.cur_version = 1
   and pr1.project_id = p.project_id 
   and pr1.placed = 1
   and pr2.project_id = p.project_id 
   and pr2.placed = 2
   and pi1.cur_version = 1
   and pr1.user_id = u1.user_id
   and pr2.user_id = u2.user_id
   and p.cur_version = 1
   and p.phase_instance_id = pi2.phase_instance_id
   and pi2.cur_version = 1
   and pi2.phase_id = rp.review_phase_id 
   and p.project_stat_id in (1,3)
   and p.project_type_id+111 = ph.phase_id
   and ph.phase_id = @ph@
   and p.comp_vers_id = cv.comp_vers_id
   and cv.component_id = cc.component_id
   and cc.root_category_id = c.category_id
   and c.category_id in  (8459260,5801776,5801777,5801778,5801779)
   and p.project_id not in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21632776, 21657411, 21657449, 21663385, 21663427, 21771067, 21771121)
   and pi1.start_date < current
 order by final_review_end_date asc,contest_status,0,null,null