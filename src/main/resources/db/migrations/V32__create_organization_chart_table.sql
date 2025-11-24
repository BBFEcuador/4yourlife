CREATE TABLE  IF NOT EXISTS organization_chart(
    id VARCHAR(100) PRIMARY KEY,
    team_id VARCHAR(100) NOT NULL,
    course_level VARCHAR(50) NOT NULL,
    CONSTRAINT fk_orgchart_team FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE  IF NOT EXISTS chart_nodes(
    id VARCHAR(100) PRIMARY KEY,
    parent_id VARCHAR(100),
    parent_node_id VARCHAR(100),
    member_id VARCHAR(100) NOT NULL,
    level VARCHAR(50) NOT NULL,
    is_captain BOOLEAN,
    organization_chart_id VARCHAR(100) NOT NULL,
    CONSTRAINT fk_chartnode_member FOREIGN KEY (member_id) REFERENCES users(id),
    CONSTRAINT fk_chartnode_orgchart FOREIGN KEY (organization_chart_id) REFERENCES organization_chart(id)
);
