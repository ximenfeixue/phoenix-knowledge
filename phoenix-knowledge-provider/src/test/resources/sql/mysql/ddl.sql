DELETE  FROM tb_column_visible  WHERE  column_id IN(5,8,9,11);
UPDATE tb_column t
SET t.columnName = '新材料',
  t.path_name = '新材料',
  t.del_status = 0
WHERE t.id = 5
	 