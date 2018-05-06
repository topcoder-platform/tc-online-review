-- **********************************************
-- Review feedbacks for Design projects
-- **********************************************

-- **********************************************
-- Review feedbacks for Development projects
-- **********************************************

-- **********************************************
-- Review feedbacks for Assembly projects
-- **********************************************
-- super always has score of 2 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29901, 200001, 132457, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29902, 200007, 132457, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29903, 200008, 132457, 2, 'Good', 132456, CURRENT);
-- user always has score of 1 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29904, 200001, 132458, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29905, 200007, 132458, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29906, 200008, 132458, 1, 'Average', 132456, CURRENT);
-- heffan always has score of 0 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29907, 200001, 132456, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29908, 200007, 132456, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29909, 200008, 132456, 0, 'Bad', 132456, CURRENT);
-- Hung always has score of 2 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29910, 200001, 124764, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29911, 200007, 124764, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29912, 200008, 124764, 2, 'Good', 132456, CURRENT);
-- Partha always has score of 1 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29913, 200001, 124772, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29914, 200007, 124772, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29915, 200008, 124772, 1, 'Average', 132456, CURRENT);
-- sandking always has score of 0 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29916, 200001, 124776, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29917, 200007, 124776, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29918, 200008, 124776, 0, 'Bad', 132456, CURRENT);
-- reassembler always has score of 2 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29919, 200001, 124835, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29920, 200007, 124835, 2, 'Good', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29921, 200008, 124835, 2, 'Good', 132456, CURRENT);
-- wyzmo always has score of 1 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29922, 200001, 124856, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29923, 200007, 124856, 1, 'Average', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29924, 200008, 124856, 1, 'Average', 132456, CURRENT);
-- Yoshi always has score of 0 for Assembly projects
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29925, 200001, 124916, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29926, 200007, 124916, 0, 'Bad', 132456, CURRENT);
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29927, 200008, 124916, 0, 'Bad', 132456, CURRENT);
-- lightspeed always has score of 2 for Assembly projects but has number of ratings less than minimum allowed
INSERT INTO review_feedback (review_feedback_id, project_id, reviewer_user_id, score, feedback_text, create_user, create_date)
  VALUES (29928, 200001, 124834, 2, 'Good', 132456, CURRENT);


-- **********************************************
-- Review applications for Design review auctions
-- **********************************************

-- ***************************************************
-- Review applications for Development review auctions
-- ***************************************************
-- Auction 100011: there is just a single PENDING review application for each review application role, e.g. there is
-- just a single candidate for each open review position
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29901, 132456, 100011, 3, 1, CURRENT); -- Primary Failure Reviewer (heffan)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29902, 132457, 100011, 4, 1, CURRENT); -- Accuracy Reviewer (super) 
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29903, 132458, 100011, 5, 1, CURRENT); -- Stress Reviewer (user)

-- ************************************************
-- Review applications for Assembly review auctions
-- ************************************************
-- Auction 100003: there is just more than one review application for a single review application role but a single
-- user applies just to a single review application role
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29904, 132457, 100003, 1, 1, CURRENT); -- Primary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29905, 132458, 100003, 1, 1, CURRENT); -- Primary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29906, 132456, 100003, 1, 1, CURRENT); -- Primary Reviewer (heffan)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29907, 124764, 100003, 2, 1, CURRENT); -- Secondary Reviewer (Hung)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29908, 124772, 100003, 2, 1, CURRENT); -- Secondary Reviewer (Partha)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29909, 124776, 100003, 2, 1, CURRENT); -- Secondary Reviewer (sandking)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29910, 124835, 100003, 2, 1, CURRENT); -- Secondary Reviewer (reassembler)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29911, 124856, 100003, 2, 1, CURRENT); -- Secondary Reviewer (wyzmo)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29912, 124916, 100003, 2, 1, CURRENT); -- Secondary Reviewer (Yoshi)
-- Auction 100010: the review applications are for a single open review position only so that there are open review 
-- positions which do not have review applications set for
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29913, 132457, 100010, 1, 1, CURRENT); -- Primary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29914, 132458, 100010, 1, 1, CURRENT); -- Primary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29915, 132456, 100010, 1, 1, CURRENT); -- Primary Reviewer (heffan)
  
-- Auction 100015: each user has review applications for several review application roles for review auction
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29916, 132458, 100015, 1, 1, CURRENT); -- Primary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29917, 132456, 100015, 1, 1, CURRENT); -- Primary Reviewer (heffan)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29918, 124764, 100015, 2, 1, CURRENT); -- Secondary Reviewer (Hung)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29919, 124772, 100015, 2, 1, CURRENT); -- Secondary Reviewer (Partha)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29920, 124776, 100015, 2, 1, CURRENT); -- Secondary Reviewer (sandking)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29921, 124835, 100015, 2, 1, CURRENT); -- Secondary Reviewer (reassembler)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29922, 124856, 100015, 2, 1, CURRENT); -- Secondary Reviewer (wyzmo)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29923, 124916, 100015, 2, 1, CURRENT); -- Secondary Reviewer (Yoshi)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29924, 132457, 100015, 2, 1, CURRENT); -- Secondary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29925, 132458, 100015, 2, 1, CURRENT); -- Secondary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29926, 132456, 100015, 2, 1, CURRENT); -- Secondary Reviewer (heffan)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29927, 124764, 100015, 1, 1, CURRENT); -- Primary Reviewer (Hung)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29928, 124772, 100015, 1, 1, CURRENT); -- Primary Reviewer (Partha)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29929, 124776, 100015, 1, 1, CURRENT); -- Primary Reviewer (sandking)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29930, 124835, 100015, 1, 1, CURRENT); -- Primary Reviewer (reassembler)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29931, 124856, 100015, 1, 1, CURRENT); -- Primary Reviewer (wyzmo)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29932, 124916, 100015, 1, 1, CURRENT); -- Primary Reviewer (Yoshi)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29933, 132457, 100015, 1, 1, CURRENT); -- Primary Reviewer (super)
-- Auction 100017: there is just more than one review application for a single review application role but a single
-- user applies just to a single review application role. Also there are review applications from users who do not
-- have reviewer ratings yet
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29934, 132457, 100017, 1, 1, CURRENT); -- Primary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29935, 132458, 100017, 1, 1, CURRENT); -- Primary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29936, 132456, 100017, 1, 1, CURRENT); -- Primary Reviewer (heffan)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29937, 124764, 100017, 2, 1, CURRENT); -- Secondary Reviewer (Hung)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29938, 124772, 100017, 2, 1, CURRENT); -- Secondary Reviewer (Partha)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29939, 124776, 100017, 2, 1, CURRENT); -- Secondary Reviewer (sandking)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29940, 124835, 100017, 2, 1, CURRENT); -- Secondary Reviewer (reassembler)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29941, 124856, 100017, 2, 1, CURRENT); -- Secondary Reviewer (wyzmo)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29942, 124916, 100017, 2, 1, CURRENT); -- Secondary Reviewer (Yoshi)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29943, 124861, 100017, 2, 1, CURRENT); -- Secondary Reviewer (ksmith)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29944, 124857, 100017, 2, 1, CURRENT); -- Secondary Reviewer (cartajs)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29945, 124834, 100017, 2, 1, CURRENT); -- Secondary Reviewer (lightspeed)
-- Auction 100024: the review applications are for a single open review position only so that there are open review 
-- positions which do not have review applications set for
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29946, 132457, 100024, 1, 1, CURRENT); -- Primary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29947, 132458, 100024, 2, 1, CURRENT); -- Secondary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29948, 132456, 100024, 2, 1, CURRENT); -- Secondary Reviewer (heffan)
-- Auction 100027: the review applications are for a single open review position only so that there are open review 
-- positions which do not have review applications set for
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29949, 132457, 100027, 1, 1, CURRENT); -- Primary Reviewer (super)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29950, 132458, 100027, 2, 1, CURRENT); -- Secondary Reviewer (user)
INSERT INTO review_application (review_application_id, user_id, review_auction_id, review_application_role_id,
                                review_application_status_id, create_date)
  VALUES (29951, 132456, 100027, 2, 1, CURRENT); -- Secondary Reviewer (heffan)
